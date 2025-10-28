package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarImageResponseDTO;
import com.spring.jwt.car.services.CarImageService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/car-images")
@RequiredArgsConstructor
public class CarImageController {

    private final CarImageService carImageService;

    //********************************************************//
    // Author : Abhishek Waghmare
    // Car Image Controller
    // Date : 24/10/2025
    //********************************************************//

    @PostMapping("/{id}/upload")
    public ResponseEntity<CarImageResponseDTO> uploadCarImages(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {

        // Upload the images and get their URLs
        List<String> urls = carImageService.uploadCarImages(id, files);

        CarImageResponseDTO response = CarImageResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Images uploaded successfully")
                .images(urls)
                .build();

        return ResponseEntity.ok(response);
    }


    //To delete imagee By Image ID
    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<BaseResponseDTO> deleteCarImage(@PathVariable Long imageId) {
        carImageService.deleteCarImage(imageId);

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Car image deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
