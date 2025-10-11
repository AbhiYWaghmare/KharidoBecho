package com.spring.jwt.Bike.Service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BikeImageService {


    // Upload multiple images for a given bike
    public List<String> uploadFiles(Long bikeId, List<MultipartFile> files,String type);



    // Delete a bike image
    Map<String, Object> delete(int imageId);

    // Fetch all images for a specific bike
    Map<String, Object> findByBikeId(Long bikeId);
}
