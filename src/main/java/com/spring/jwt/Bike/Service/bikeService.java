package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.dto.bikeDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface bikeService {
    // Method For Create a New Bike
    bikeDto createBike(bikeDto bikedto);

    // Method For get a bike
    List<bikeDto> getAllBikes();

    // Method For get Bike By id
    bikeDto getBikeById(Long bike_id);

    // Method For Update the Bike
    bikeDto updateBike(Long bike_id , bikeDto bikedto);

    // Delete the Bike soft delete
    bikeDto softDeletebike(Long bike_id);

    // Hard Delete Bike
    void hardDeleteBike(Long bike_id);

    Page<bikeDto> getBikesBySellerAndStatus(Long sellerId, bikeStatus status, int page, int size);
    Page<bikeDto> getBikesBySeller(Long sellerId, int page, int size);



    Page<bikeDto> getBikesByStatus(bikeStatus status, int page, int size);

    Long countBikesBySellerAndStatus(Long sellerId, bikeStatus status);


}
