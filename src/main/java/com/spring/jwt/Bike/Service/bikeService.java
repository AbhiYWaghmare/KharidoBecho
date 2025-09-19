package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.dto.bikeDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface bikeService {
    // Method For Create a New Bike
    bikeDto createBike(bikeDto bikedto);

    // Method For get a bike
    List<bikeDto> getAllBikes();

    // Method For get Bike By Id
    bikeDto getBikeById(Long id);

    // Method For Update the Bike
    bikeDto updateBike(Long id , bikeDto bikedto);

    // Delete the Bike soft delete
    bikeDto softDeletebike(Long id);

    // Hard Delete Bike
    void hardDeleteBike(Long id);

    Page<bikeDto> getBikesBySellerAndStatus(Long sellerId, bikeStatus status, int page, int size);

    Page<bikeDto> getBikesByStatus(bikeStatus status, int page, int size);

    Long countBikesBySellerAndStatus(Long sellerId, bikeStatus status);

}
