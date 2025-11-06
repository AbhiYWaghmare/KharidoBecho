package com.spring.jwt.laptop.controller;

import com.cloudinary.Cloudinary;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
import com.spring.jwt.laptop.entity.LaptopBooking;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


//********************************************************//

    //Author : Sudhir Lokade
    //Laptop Photo Controller
    //Date : 24/09/2025

//*******************************************************//

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/laptop-photo")
public class LaptopPhotoController {

    private final LaptopPhotoService laptopPhotoService;

    private final Cloudinary cloudinary;

    LaptopBooking booking;
    private static final long MAX_FILE_SIZE = 400 * 1024;


    //====================================================//
    //  Upload image of Laptop                            //
    //  Post /api/photo/upload                            //
    //====================================================//

    //To upload images of particular laptop by ID
    @PostMapping("/upload")
    public ResponseEntity<LaptopResponseDTO> uploadImages(@RequestParam Long laptopId, @RequestParam("files") List<MultipartFile> files, HttpServletRequest httpServletRequest) {
        List<String> photos = laptopPhotoService.uploadPhoto(laptopId, files);
        String imageUrl = String.join(", ", photos);

        LaptopResponseDTO laptopResponseDTO = new LaptopResponseDTO(
                "success",
                "Laptop photos uploaded successfully for laptop id " + laptopId,
                "CREATED",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                "NULL",
                httpServletRequest.getRequestURI(),
                imageUrl,
                laptopId

        );
        return ResponseEntity.ok(laptopResponseDTO);
    }


    //====================================================//
    //  Delete photo of Laptop by id                      //
    //  Delete /api/photo/delete                          //
    //====================================================//

    //To delete images By Image ID
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteImage(@RequestParam Long photoId) {
        laptopPhotoService.deleteImage(photoId);

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Image deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
