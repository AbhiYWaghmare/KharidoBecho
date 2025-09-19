package com.spring.jwt.Bike.Controller;

import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Service.bikeService;
import com.spring.jwt.Bike.dto.bikeDto;
import com.spring.jwt.Bike.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bikes")
public class bikeController {

    private final bikeService bikeService;

    public bikeController(bikeService bikeService) {
        this.bikeService = bikeService;
    }

    /** CREATE Bike */
    @PostMapping(value = "/post", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> createBike(@RequestBody bikeDto bikedto) {
        bikeService.createBike(bikedto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("SUCCESS", "Bike created successfully"));
    }


    /** GET Bike by ID */
    @GetMapping("/get/{id}")
    public ResponseEntity<bikeDto> getBikeById(@PathVariable Long id) {
        bikeDto bike = bikeService.getBikeById(id);
        return ResponseEntity.ok(bike); // For GET, you can still return DTO
    }

    /** UPDATE Bike */
    @PatchMapping("/patch/{id}")
    public ResponseEntity<ApiResponse> updateBike(@PathVariable Long id, @RequestBody bikeDto bikedto) {
        bikeService.updateBike(id, bikedto);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Bike updated successfully"));
    }

    /** SOFT DELETE Bike */
    @PatchMapping("/patch/{id}/soft-delete")
    public ResponseEntity<ApiResponse> softDeleteBike(@PathVariable Long id) {
        bikeService.softDeletebike(id);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Bike soft deleted successfully"));
    }

    /** HARD DELETE Bike */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> hardDeleteBike(@PathVariable Long id) {
        bikeService.hardDeleteBike(id);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Bike permanently deleted"));
    }

    /** GET all bikes */
    @GetMapping("/get")
    public ResponseEntity<List<bikeDto>> getAllBikes() {
        List<bikeDto> bikes = bikeService.getAllBikes();
        return ResponseEntity.ok(bikes); // For GET, return DTO
    }

    /** GET Bikes by Seller & Status with Pagination */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<bikeDto>> getBikesBySellerAndStatus(
            @PathVariable Long sellerId,
            @RequestParam bikeStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<bikeDto> bikes = bikeService.getBikesBySellerAndStatus(sellerId, status, page, size);
        return ResponseEntity.ok(bikes);
    }

    /** GET Bikes by Status with Pagination */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<bikeDto>> getBikesByStatus(
            @PathVariable bikeStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<bikeDto> bikes = bikeService.getBikesByStatus(status, page, size);
        return ResponseEntity.ok(bikes);
    }

    /** COUNT Bikes by Seller & Status */
    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> countBikesBySellerAndStatus(
            @PathVariable Long sellerId,
            @RequestParam bikeStatus status
    ) {
        Long count = bikeService.countBikesBySellerAndStatus(sellerId, status);
        return ResponseEntity.ok(count);
    }
}
