package com.spring.jwt.Bike.Service;

import com.cloudinary.Cloudinary;
import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.BikeImage;
import com.spring.jwt.Bike.Exceptions.BikeImageNotFound;
import com.spring.jwt.Bike.Exceptions.bikeNotFoundException;
import com.spring.jwt.Bike.Repository.BikeImageRepository;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
public class BikeImageServiceImpl implements BikeImageService {

    private final BikeImageRepository bikeImageRepository;
    private final bikeRepository bikeRepository;
    private final Cloudinary cloudinary;
    private static final long MAX_IMAGE_BYTES = 400 * 1024;

    /**
     * Upload a  file
     */
    @Override
    @Transactional
    public List<String> uploadFiles(Long bikeId, List<MultipartFile> files,String type) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found with id: " + bikeId));
       /*
       * create a list for result */
        List<String> urls = new ArrayList<>();
        /*
         * cheak the files is empty or null
         */
        for (MultipartFile file : files) {
            // Validation
            if (file == null || file.isEmpty()) {
                throw new BikeImageNotFound("One of the uploaded files is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BikeImageNotFound("Invalid file type: " + contentType);
            }

            try {
                MultipartFile toUpload = file;

                // If file size is large, compress it
                if (file.getSize() > MAX_IMAGE_BYTES) {
                    byte[] compressed = compressImageToMaxSize(file, MAX_IMAGE_BYTES);
                    if (compressed == null || compressed.length == 0) {
                        throw new BikeImageNotFound("Failed to compress image: " + file.getOriginalFilename());
                    }
                    if (compressed.length > MAX_IMAGE_BYTES) {
                        throw new BikeImageNotFound("Cannot reduce image below 400KB: " + file.getOriginalFilename());
                    }

                    // Wrap compressed bytes into MultipartFile
                    toUpload = new ByteArrayMultipartFile(
                            compressed,
                            file.getOriginalFilename(),
                            file.getContentType()
                    );
                }

                // Upload to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        toUpload.getBytes(),
                        Map.of("folder", "bike_images", "resource_type", "auto")
                );
             // returns cloudinary for saving in database
                String imageLink = Objects.toString(uploadResult.get("secure_url"), null);
                String publicId = Objects.toString(uploadResult.get("public_id"), null);

                // Save BikeImage entity in database
                BikeImage bikeImage = BikeImage.builder()
                        .type(type)
                        .image_link(imageLink)
                        .publicId(publicId)
                        .bike(bike)
                        .build();
                bikeImageRepository.save(bikeImage);

                urls.add(imageLink);


            } catch (IOException e) {
                throw new BikeImageNotFound("Failed to upload image: " + file.getOriginalFilename());
            }
        }

        return urls;
    }


// ================= Helper Method for compressing the image =================
    // this is an image compression method
    private byte[] compressImageToMaxSize(MultipartFile file, long maxBytes) throws IOException {
        // buffered image reads the file and get the image as jpg
             // reads the input image
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) return null;

        String formatName = "jpg";  // output will be jpg for the all type images
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        float quality = 0.90f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //holds memory output compress image
         // compress the image
        for (int attempt = 0; attempt < 10; attempt++) {
            baos.reset();// reset previous output for new
              // creates new blank image output
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(inputImage, 0, 0, width, height, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            if (!writers.hasNext()) break;
            ImageWriter writer = writers.next();
            // set compression quality
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);//mannually set compression level
                param.setCompressionQuality(Math.max(0.10f, quality));
            }

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(outputImage, null, null), param);
                writer.dispose(); // resources
            }
            // cheak if size limit is reached
            if (baos.size() <= maxBytes) {
                return baos.toByteArray();
            }
             // or reduce quality
            if (quality > 0.35f) {
                quality -= 0.15f;
            } else {
                width = Math.max(100, (int) (width * 0.9));
                height = Math.max(100, (int) (height * 0.9));
            }
        }
            // return
        return baos.size() > 0 ? baos.toByteArray() : null;
    }


// ================= Helper Class =================
    // this is helper class and it is used to convert the byte
    private static class ByteArrayMultipartFile implements MultipartFile {
        // fields and constructor
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



    /**
     * Delete an image from Cloudinary and DB
     */
    @Override
    public Map<String, Object> delete(int imageId) {
        BikeImage bikeImage = bikeImageRepository.findById(imageId)
                .orElseThrow(() -> new BikeImageNotFound("Bike image not found with imageId: " + imageId));

        try {
            Map result = cloudinary.uploader().destroy(bikeImage.getPublicId(), Map.of());

            if ("ok".equals(result.get("result"))) {
                bikeImageRepository.delete(bikeImage);
                return Map.of(
                        "message", "Image deleted successfully",
                        "imageId", imageId
                );
            } else {
                return Map.of(
                        "message", "Failed to delete image from Cloudinary",
                        "imageId", imageId
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete bike image: " + e.getMessage(), e);
        }
    }


    /**
     * Fetch all images for a bike
     */
    @Override
    public Map<String, Object> findByBikeId(Long bikeId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + bikeId));

        List<BikeImage> images = bikeImageRepository.findByBike(bike);

        return Map.of(
                "bikeId", bike.getBike_id(),
                "images", images
        );
    }
}
