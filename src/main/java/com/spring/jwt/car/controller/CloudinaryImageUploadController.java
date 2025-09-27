package com.spring.jwt.car.controller;

import com.spring.jwt.car.entity.CloudinaryImage;
import com.spring.jwt.car.service.CloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryImageUploadController {
    @Autowired
    private CloudinaryImageService imageService;

    private final CloudinaryImageService cloudinaryImageService;

    public CloudinaryImageUploadController(CloudinaryImageService cloudinaryImageService) {
        this.cloudinaryImageService = cloudinaryImageService;
    }


    // POST - Upload
    @PostMapping("/upload/{carId}")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("image") MultipartFile file,
            @PathVariable int carId) {
        Map<String, Object> data = cloudinaryImageService.upload(file, carId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // DELETE - Delete
    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable String publicId) {
        Map<String, Object> result = cloudinaryImageService.delete(publicId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // ✅ GET - fetch all images
    @GetMapping("/all")
    public ResponseEntity<List<CloudinaryImage>> getAllImages() {
        List<CloudinaryImage> images = cloudinaryImageService.getAllImages();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    // ✅ GET - fetch one image by ID
    @GetMapping("/{carId}")
    public ResponseEntity<?> getImagesByCar(@PathVariable Long carId) {
        List<CloudinaryImage> images = cloudinaryImageService.getImagesByCarId(carId);

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No images found for carId: " + carId);
        }
        return ResponseEntity.ok(images);
    }
}
