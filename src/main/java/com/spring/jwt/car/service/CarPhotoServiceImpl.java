package com.spring.jwt.car.service;

import com.spring.jwt.car.entity.CarPhotos;
import com.spring.jwt.car.exception.CarImageException;
import com.spring.jwt.car.exception.CarNotFoundException;
import com.spring.jwt.car.repository.CarPhotoRepository;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.entity.Car;
import com.spring.jwt.utils.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Service
@AllArgsConstructor
public class CarPhotoServiceImpl implements CarPhotoService {

    private final CarPhotoRepository carPhotoRepository;
    private final CarRepository carRepository;
    private final CloudinaryService cloudinaryService;

    private static final long MAX_IMAGE_BYTES = 400 * 1024L; // 400 KB limit

    //====================================================//
    // Upload multiple Car Photos with compression support
    //====================================================//
    @Override
    @Transactional
    @Builder
    public List<String> uploadPhoto(Long carId, List<MultipartFile> files) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                throw new CarImageException("One of the uploaded files is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new CarImageException("Invalid file type: " + contentType);
            }

            try {
                MultipartFile toUpload = file;

                // Compress if file exceeds 400 KB
                if (file.getSize() > MAX_IMAGE_BYTES) {
                    byte[] compressed = compressImageToMaxSize(file, MAX_IMAGE_BYTES);
                    if (compressed == null || compressed.length == 0) {
                        throw new CarImageException("Failed to compress image: " + file.getOriginalFilename());
                    }
                    if (compressed.length > MAX_IMAGE_BYTES) {
                        throw new CarImageException("Cannot reduce image below 400KB: " + file.getOriginalFilename());
                    }

                    toUpload = new ByteArrayMultipartFile(
                            compressed,
                            file.getOriginalFilename(),
                            file.getContentType()
                    );
                }

                // Upload to Cloudinary and get response
                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(toUpload, "car_photo");
                String imageUrl = Objects.toString(uploadResult.get("secure_url"), null);
                String publicId = Objects.toString(uploadResult.get("public_id"), null);

                // Save photo record in DB
                CarPhotos carPhotos = CarPhotos.builder()
                        .photoUrl(imageUrl)
                        .publicId(publicId)
                        .car(car)
                        .build();
                carPhotoRepository.save(carPhotos);

                urls.add(imageUrl);

            } catch (IOException e) {
                throw new CarImageException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        return urls;
    }

    //====================================================//
    // Compress image dynamically to stay under maxBytes
    //====================================================//
    private byte[] compressImageToMaxSize(MultipartFile file, long maxBytes) throws IOException {
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) return null;

        String formatName = "jpg";
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        float quality = 0.90f;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int attempt = 0; attempt < 10; attempt++) {
            baos.reset();

            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(inputImage, 0, 0, width, height, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            if (!writers.hasNext()) break;
            ImageWriter writer = writers.next();

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0.10f, quality));
            }

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(outputImage, null, null), param);
                writer.dispose();
            }

            if (baos.size() <= maxBytes) {
                return baos.toByteArray();
            }

            // Reduce quality/dimensions gradually
            if (quality > 0.35f) {
                quality -= 0.15f;
            } else {
                width = Math.max(100, (int) (width * 0.9));
                height = Math.max(100, (int) (height * 0.9));
            }
        }

        return baos.size() > 0 ? baos.toByteArray() : null;
    }

    //====================================================//
    // Custom MultipartFile wrapper for compressed images
    //====================================================//
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] bytes;
        private final String originalFilename;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] bytes, String originalFilename, String contentType) {
            this.bytes = bytes;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        @Override
        public String getName() { return originalFilename; }

        @Override
        public String getOriginalFilename() { return originalFilename; }

        @Override
        public String getContentType() { return contentType; }

        @Override
        public boolean isEmpty() { return bytes == null || bytes.length == 0; }

        @Override
        public long getSize() { return bytes.length; }

        @Override
        public byte[] getBytes() { return bytes; }

        @Override
        public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(bytes);
            }
        }
    }

    //====================================================//
    // Delete Car Photo by ID (Cloudinary + DB)
    //====================================================//
    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        CarPhotos carPhotos = carPhotoRepository.findById(imageId)
                .orElseThrow(() -> new CarImageException("Car image not found with ID: " + imageId));

        try {
            // Delete from Cloudinary first
            boolean deleted = cloudinaryService.deleteByPublicId(carPhotos.getPublicId());
            if (!deleted) {
                throw new CarImageException("Failed to delete image from Cloudinary: " + carPhotos.getPublicId());
            }

            // Then delete from DB
            carPhotoRepository.delete(carPhotos);

        } catch (IOException e) {
            throw new CarImageException("Error deleting image from Cloudinary: " + carPhotos.getPublicId(), e);
        }
    }

    //====================================================//
    // Upload single Car Image (optional helper)
    //====================================================//
    public CarPhotos uploadCarImage(Long carId, MultipartFile file) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        try {
            String imageUrl = cloudinaryService.uploadFile(file, "car_photo");
            CarPhotos image = new CarPhotos();
            image.setCar(car);
//            image.setPhotoLink(imageUrl);
            return carPhotoRepository.save(image);
        } catch (IOException e) {
            throw new CarImageException("Failed to upload image: " + file.getOriginalFilename(), e);
        }
    }
}
