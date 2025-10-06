package com.spring.jwt.car.controller;

import com.spring.jwt.car.entity.CloudinaryImage;
import com.spring.jwt.car.response.ApiResponse;
import com.spring.jwt.car.service.CloudinaryImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryImageController {

    private final CloudinaryImageService cloudinaryImageService;

    public CloudinaryImageController(CloudinaryImageService cloudinaryImageService) {
        this.cloudinaryImageService = cloudinaryImageService;
    }

    // ------------------- POST - Upload Image -------------------
    @PostMapping("/upload/{carId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadImage(
            @RequestParam("image") MultipartFile file,
            @PathVariable int carId) {

        Map<String, Object> data = cloudinaryImageService.upload(file, carId);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                "SUCCESS",
                "Image uploaded successfully",
                data
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ------------------- DELETE - Delete Image -------------------
    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteImage(@PathVariable String publicId) {
        Map<String, Object> result = cloudinaryImageService.delete(publicId);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                "SUCCESS",
                "Image deleted successfully",
                result
        );

        return ResponseEntity.ok(response);
    }

    // ------------------- GET - All Images -------------------
    @GetMapping("/all")
    public ResponseEntity<List<CloudinaryImage>> getAllImages() {
        List<CloudinaryImage> images = cloudinaryImageService.getAllImages();
        return ResponseEntity.ok(images); // raw JSON response
    }

    // ------------------- GET - Images by Car -------------------
    @GetMapping("/{carId}")
    public ResponseEntity<?> getImagesByCar(@PathVariable Long carId) {
        List<CloudinaryImage> images = cloudinaryImageService.getImagesByCarId(carId);

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No images found for carId: " + carId));
        }

        return ResponseEntity.ok(images); // raw JSON response
    }
}










