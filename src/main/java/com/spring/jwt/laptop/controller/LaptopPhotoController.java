package com.spring.jwt.laptop.controller;

import com.cloudinary.Cloudinary;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//********************************************************//

    //Author : Sudhir Lokade
    //Laptop Photo Controller
    //Date : 24/09/2025

//*******************************************************//

@RestController
@AllArgsConstructor
@RequestMapping("/api/photo")
public class LaptopPhotoController {

    private final LaptopPhotoService laptopPhotoService;

    private final Cloudinary cloudinary;


    //====================================================//
    //  Upload image of Laptop                            //
    //  Post /api/photo/upload                            //
    //====================================================//
    @PostMapping("/upload")
    public ResponseEntity<List<LaptopPhotos>> uploadPhotos(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("laptopId") Long laptopId,
            @RequestParam("type") String type) throws IOException {

        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        List<LaptopPhotos> photos = laptopPhotoService.uploadFile(files, laptopId, type);
        return ResponseEntity.ok(photos);
    }

    //====================================================//
    //  Delete photo of Laptop by id                      //
    //  Delete /api/photo/delete                          //
    //====================================================//
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteFile(
            @RequestParam int laptopId,
            @RequestParam int photoId) {

        Map response = laptopPhotoService.deleteFile(laptopId, photoId);
        return ResponseEntity.ok(response);

    }

}
