package com.spring.jwt.car.controller;

import com.cloudinary.Cloudinary;
import com.spring.jwt.car.dto.CarResponseDTO;
import com.spring.jwt.car.service.CarPhotoService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/car/photo")
public class CarPhotoController {

    private final CarPhotoService carPhotoService;
    private final Cloudinary cloudinary;

    // UPLOAD CAR PHOTOS
    @PostMapping("/upload")
    public ResponseEntity<CarResponseDTO> uploadCarImages(
            @RequestParam Long carId,
            @RequestParam("files") List<MultipartFile> files,
            HttpServletRequest request) throws IOException {

        List<String> photoUrls = carPhotoService.uploadPhoto(carId, files);
        String imageUrl = String.join(", ", photoUrls);

        CarResponseDTO response = new CarResponseDTO(
                "success",
                "Car photos uploaded successfully for car ID " + carId,
                "CREATED",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                "NULL",
                request.getRequestURI(),
                imageUrl
        );

        return ResponseEntity.ok(response);
    }

    // DELETE CAR PHOTO
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteCarImage(@RequestParam Long photoId) {
        carPhotoService.deleteImage(photoId);

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Car image deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
