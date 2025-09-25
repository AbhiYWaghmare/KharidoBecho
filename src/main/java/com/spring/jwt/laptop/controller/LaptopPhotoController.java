package com.spring.jwt.laptop.controller;

import com.cloudinary.Cloudinary;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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
    public ResponseEntity<Map> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("laptopId") int laptopId,
            @RequestParam("type") String type) throws IOException {
        Map data = this.laptopPhotoService.uploadFile(file,laptopId,type);
        return new ResponseEntity<>(data, HttpStatus.OK);

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
