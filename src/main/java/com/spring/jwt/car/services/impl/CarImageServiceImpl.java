package com.spring.jwt.car.services.impl;

import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.dto.CarImageResponseDTO;
import com.spring.jwt.car.entity.CarImage;
import com.spring.jwt.exception.car.CarImageException;
import com.spring.jwt.car.repository.CarImageRepository;
import com.spring.jwt.car.services.CarImageService;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.exception.car.CarNotFoundException;
import com.spring.jwt.utils.CloudinaryService;
import com.spring.jwt.utils.ByteArrayMultipartFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarImageServiceImpl implements CarImageService {

    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;
    private final CloudinaryService cloudinaryService;

    // 400 KB max per image
    private static final long MAX_IMAGE_BYTES = 400 * 1024;

    @Override
    @Transactional
    public List<String> uploadCarImages(Long carId, List<MultipartFile> files) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            // ===== Basic validation =====
            if (file == null || file.isEmpty()) {
                throw new CarImageException("One of the uploaded files is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new CarImageException("Invalid file type: " + contentType);
            }

            try {
                MultipartFile toUpload = file;

                // If larger than max, compress
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

                // ===== Upload to Cloudinary =====
                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(toUpload, "cars");
                String imageUrl = Objects.toString(uploadResult.get("secure_url"), null);
                String publicId = Objects.toString(uploadResult.get("public_id"), null);

                // ===== Save in DB =====
                CarImage carImage = CarImage.builder()
                        .imageUrl(imageUrl)
                        .publicId(publicId)
                        .car(car)
                        .build();
                carImageRepository.save(carImage);

                urls.add(imageUrl);

            } catch (IOException e) {
                throw new CarImageException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        return urls;
    }

    @Override
    @Transactional
    public void deleteCarImage(Long imageId) {
        CarImage image = carImageRepository.findById(imageId)
                .orElseThrow(() -> new CarImageException("Car image not found with ID: " + imageId));

        try {
            // Delete from Cloudinary first
            boolean deleted = cloudinaryService.deleteByPublicId(image.getPublicId());
            if (!deleted) {
                throw new CarImageException("Failed to delete image from Cloudinary: " + image.getPublicId());
            }

            // Then delete from DB
            carImageRepository.delete(image);

        } catch (IOException e) {
            throw new CarImageException("Error deleting image from Cloudinary: " + image.getPublicId(), e);
        }
    }

    // ===== Helper method for compression =====
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

            if (baos.size() <= maxBytes) return baos.toByteArray();

            if (quality > 0.35f) {
                quality -= 0.15f;
            } else {
                width = Math.max(100, (int) (width * 0.9));
                height = Math.max(100, (int) (height * 0.9));
            }
        }

        return baos.size() > 0 ? baos.toByteArray() : null;
    }
}
