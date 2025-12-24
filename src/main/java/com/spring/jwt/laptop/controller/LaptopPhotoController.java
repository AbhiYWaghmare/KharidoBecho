package com.spring.jwt.laptop.controller;

import com.spring.jwt.laptop.dto.ImageUploadResponseDTO;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import com.spring.jwt.utils.BaseResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //====================================================//
    //  Upload image of Laptop                            //
    //  POST /laptop-photo/upload                         //
    //====================================================//

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ImageUploadResponseDTO> uploadLaptopImages(
            @RequestParam("laptopId") Long laptopId,
            @RequestParam("files") List<MultipartFile> files) {

        List<String> urls = laptopPhotoService.uploadPhoto(laptopId, files);

        ImageUploadResponseDTO response = ImageUploadResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Image uploaded successfully")
                .images(urls)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //====================================================//
    //  Delete photo of Laptop by id                      //
    //  DELETE /laptop-photo/delete                       //
    //====================================================//

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
