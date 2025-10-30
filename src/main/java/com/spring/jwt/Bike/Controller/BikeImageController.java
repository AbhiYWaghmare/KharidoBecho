package com.spring.jwt.Bike.Controller;

import com.cloudinary.Cloudinary;

import com.spring.jwt.Bike.Entity.BikeImage;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.Service.BikeImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/bikes/image")
public class BikeImageController {

    private final BikeImageService bikeImageService;


    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPhoto(@RequestParam("files") MultipartFile[] files,
                                                  @RequestParam("bikeId") Long bikeId
                                                  )
    {
        List<String> uploadedUrls = bikeImageService.uploadFiles(bikeId, Arrays.asList(files) );
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "message", "Images uploaded successfully",
                        "bikeId", bikeId,
                        "uploadedUrls", uploadedUrls

                ));

    }






    // Get all images for a specific bike
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getImagesByBikeId(
            @RequestParam("bikeId") Long Id) {

        Map<String, Object> data = bikeImageService.findByBikeId(Id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    //  Delete a bike image by imageId
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteBikeImage(
            @RequestParam("imageId") int imageId) {

        Map<String, Object> response = bikeImageService.delete(imageId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
