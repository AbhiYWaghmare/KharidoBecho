package com.spring.jwt.laptop.controller;

import com.cloudinary.Cloudinary;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.repository.LaptopPhotoRepository;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/photo")
public class LaptopPhotoController {

    private final LaptopPhotoService laptopPhotoService;
    private final LaptopPhotoRepository laptopPhotoRepository;

    private final Cloudinary cloudinary;
    @PostMapping("/upload")
    public ResponseEntity<Map> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("laptopId") int laptopId,
//            @RequestParam("laptop_photo_id") int photoId,
            @RequestParam("type") String type) throws IOException {
        Map data = this.laptopPhotoService.uploadFile(file,laptopId,type);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }


//        Map uploadResult;
//        try {
//            uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("resource_type", "auto"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
//        }
//
//        String secureUrl = (String) uploadResult.get("secure_url");
//        String publicId = (String) uploadResult.get("public_id");
//
//        if (secureUrl == null || publicId == null) {
//            throw new RuntimeException("Cloudinary returned null secure_url or public_id");
//        }
//
//        LaptopPhotos laptopPhotos = new LaptopPhotos();
//        laptopPhotos.setLaptopId(laptopId);
//        laptopPhotos.setType(type);
//        laptopPhotos.setPhoto_link(uploadResult.get("secure_url").toString());
//        laptopPhotos.setPublicId(uploadResult.get("public_id").toString());
//
//        LaptopPhotos savedPhoto = laptopPhotoRepository.save(laptopPhotos);
//        System.out.println("Saved photo: " + savedPhoto);
//
//        return ResponseEntity.ok(savedPhoto);

    @GetMapping("/delete")
    public Map deleteFile(@RequestParam int photoId) throws IOException {
        return laptopPhotoService.deleteFile(photoId);
    }
}
