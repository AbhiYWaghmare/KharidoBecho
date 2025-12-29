package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.exception.laptop.LaptopImageException;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import com.spring.jwt.utils.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.spring.jwt.laptop.repository.LaptopPhotoRepository;
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
public class LaptopPhotoServiceImpl implements LaptopPhotoService {

    public final LaptopPhotoRepository laptopPhotoRepository;
    public final CloudinaryService cloudinaryService;
    public final LaptopRepository laptopRepository;
    private static final long MAX_IMAGE_BYTES = 400 * 1024L; // 400 KB


    @Override
    @Transactional
    public List<String> uploadPhoto(Long laptopId, List<MultipartFile> files) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException(laptopId));

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            // Basic validation
            if (file == null || file.isEmpty()) {
                throw new LaptopImageException("One of the uploaded files is empty");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new LaptopImageException("Invalid file type: " + contentType);
            }

            try {
                MultipartFile toUpload = file;

                // If file is bigger than max allowed, try to compress
                if (file.getSize() > MAX_IMAGE_BYTES) {
                    byte[] compressed = compressImageToMaxSize(file, MAX_IMAGE_BYTES);
                    if (compressed == null || compressed.length == 0) {
                        throw new LaptopImageException("Failed to compress image: " + file.getOriginalFilename());
                    }
                    if (compressed.length > MAX_IMAGE_BYTES) {
                        throw new LaptopImageException("Cannot reduce image below 400KB: " + file.getOriginalFilename());
                    }
                    // wrap bytes into MultipartFile so it can be passed to CloudinaryService
                    toUpload = new ByteArrayMultipartFile(
                            compressed,
                            file.getOriginalFilename(),
                            file.getContentType()
                    );
                }

                // Upload to Cloudinary and get full result (secure_url + public_id)
                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(toUpload, "laptop_photo");
                String imageUrl = Objects.toString(uploadResult.get("secure_url"), null);
                String publicId = Objects.toString(uploadResult.get("public_id"), null);

                //  LaptopImage with both URL and publicId
                LaptopPhotos laptopPhotos = LaptopPhotos.builder()
                        .photo_link(imageUrl)
                        .publicId(publicId)
                        .laptop(laptop)
                        .build();
                laptopPhotoRepository.save(laptopPhotos);

                urls.add(imageUrl);

            } catch (IOException e) {
                throw new LaptopImageException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        return urls;
    }


    //  compress image to fit under maxBytes (tries lowering JPEG quality)(Helper)
    private byte[] compressImageToMaxSize(MultipartFile file, long maxBytes) throws IOException {

        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) return null;

        // We provide output in JPEG. This may strip transparency if source was PNG.
        String formatName = "jpg";

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        float quality = 0.90f; // start quality
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Iteratively try reducing quality and then size until under maxBytes
        for (int attempt = 0; attempt < 10; attempt++) {
            baos.reset();

            // create a scaled copy if we shrank dimensions
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(inputImage, 0, 0, width, height, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            if (!writers.hasNext()) {
                break;
            }
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0.10f, quality)); // limit lower bound
            }

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(outputImage, null, null), param);
                writer.dispose();
            }

            // If under max, return bytes
            if (baos.size() <= maxBytes) {
                return baos.toByteArray();
            }

            // otherwise reduce quality first, then dimensions
            if (quality > 0.35f) {
                quality -= 0.15f; // reduce compression quality
            } else {
                // reduce dimensions by 90% to try further reduction
                width = Math.max(100, (int) (width * 0.9));
                height = Math.max(100, (int) (height * 0.9));
            }
        }

        // final attempt: return whatever we have (may exceed maxBytes) or null
        return baos.size() > 0 ? baos.toByteArray() : null;
    }

    // Small adapter class to wrap compressed bytes into a MultipartFile
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
        public String getName() {
            return originalFilename;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes == null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(bytes);
            }
        }
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        LaptopPhotos laptopPhotos = laptopPhotoRepository.findById(imageId)
                .orElseThrow(() -> new LaptopImageException("Laptop image not found with ID: " + imageId));


        try {
            //  first image will delete from cloudnairy then DB
            boolean deleted = cloudinaryService.deleteByPublicId(laptopPhotos.getPublicId());
            if (!deleted) {
                throw new LaptopImageException("Failed to delete image from Cloudinary: " + laptopPhotos.getPublicId());
            }

            //  Then delete from DB
            laptopPhotoRepository.delete(laptopPhotos);

        } catch (IOException e) {
            throw new LaptopImageException("Error deleting image from Cloudinary: " + laptopPhotos.getPublicId(), e);
        }
    }


//    public LaptopPhotos uploadLaptopImage(Long laptopId, MultipartFile file) {
//        Laptop laptop = laptopRepository.findById(laptopId)
//                .orElseThrow(() -> new LaptopNotFoundException(laptopId));
//
//
//        try {
//            String imageUrl = cloudinaryService.uploadFile(file, "laptop_photo");
//            LaptopPhotos image = new LaptopPhotos();
//            image.setLaptop(laptop);
//            image.setPhoto_link(imageUrl);
//            return laptopPhotoRepository.save(image);
//        } catch (IOException e) {
//            throw new LaptopImageException("Failed to upload image: " + file.getOriginalFilename(), e);
//        }
//    }
}


