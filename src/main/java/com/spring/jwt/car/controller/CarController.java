//
//package com.spring.jwt.car.controller;
//
//import com.spring.jwt.car.dto.BaseResponseDTO;
//import com.spring.jwt.car.dto.CarDto;
//import com.spring.jwt.car.service.CarService;
//import com.spring.jwt.entity.Status;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//
//@RestController
//@RequestMapping("/cars")
//public class CarController {
//   // constructor injection
//    private final CarService carService;
//
//    public CarController(CarService carService) {
//        this.carService = carService;
//    }
//
//
//    // Create new car
//    @PostMapping("/newcar")
//    public ResponseEntity<CarDto> createCar(@RequestBody CarDto dto) {
//        CarDto created = carService.createCar(dto);
//        return ResponseEntity.ok(created);
//    }
//
//    // Partially update car
//    @PatchMapping("/updatecar")
//    public ResponseEntity<CarDto> updateCar(@RequestParam Integer carId,
//                                            @RequestBody CarDto dto) {
//        CarDto updated = carService.patchUpdateCar(carId, dto);
//        return ResponseEntity.ok(updated);
//    }
//
//    // Get car by  car id
//    @GetMapping("getById")
//    public ResponseEntity<CarDto> getCarById(@RequestParam Integer carId) {
//        return ResponseEntity.ok(carService.getCarById(carId));
//    }
//
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<BaseResponseDTO<CarDto>> deleteCar(
//            @RequestParam Integer carId,
//            @RequestParam String type) {
//
//        CarDto deletedCar = carService.deleteCar(carId, type);
//
//        BaseResponseDTO<CarDto> response = new BaseResponseDTO<>();
//        response.setMessage("Car deleted successfully (" + type + ")");
//        response.setData(deletedCar);
//        return ResponseEntity.ok(response);
//    }
//
//    // Get cars by seller and status
//    @GetMapping("/getcarbyseller")
//    public ResponseEntity<Page<CarDto>> getCarsBySellerAndStatus(@RequestParam Integer sellerId,
//                                                                 @RequestParam Status status,
//                                                                 @RequestParam(defaultValue = "0") int page,
//                                                                 @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(carService.getCarsBySellerAndStatus(sellerId, status, page, size));
//    }
//
//    // Get cars by status
//    @GetMapping("/GetCarbyStatus")
//    public ResponseEntity<Page<CarDto>> getCarsByStatus(@RequestParam Status status,
//                                                        @RequestParam(defaultValue = "0") int page,
//                                                        @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(carService.getCarsByStatus(status, page, size));
//    }
//
//    // Count cars by seller and status
//    @GetMapping("/CarCount")
//    public ResponseEntity<Long> countCarsBySellerAndStatus(@RequestParam Integer sellerId,
//                                                           @RequestParam Status status) {
//        return ResponseEntity.ok(carService.countCarsBySellerAndStatus(sellerId, status));
//    }
//
//    // Get car by main car ID
//    @GetMapping("/GetCarByMainCarID")
//    public ResponseEntity<CarDto> getCarByMainCarId(@RequestParam String mainCarId) {
//        return ResponseEntity.ok(carService.getByMainCarId(mainCarId));
//    }
//
//}
//
package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.car.response.ApiResponse;
import com.spring.jwt.car.service.CarService;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /** CREATE Car */
    @PostMapping("/newcar")
    public ResponseEntity<ApiResponse<Integer>> createCar(@RequestBody CarDto dto) {
        CarDto created = carService.createCar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("SUCCESS", "Car created successfully", created.getCarId()));
    }

    /** UPDATE Car (partial) */
    @PatchMapping("/updatecar")
    public ResponseEntity<ApiResponse<Integer>> updateCar(@RequestParam Integer carId,
                                                          @RequestBody CarDto dto) {
        CarDto updated = carService.patchUpdateCar(carId, dto);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car updated successfully", updated.getCarId()));
    }

    /** GET Car by ID */
    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<CarDto>> getCarById(@RequestParam Integer carId) {
        CarDto car = carService.getCarById(carId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car retrieved successfully", car));
    }

    /** DELETE Car (soft/hard) */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Integer>> deleteCar(@RequestParam Integer carId,
                                                          @RequestParam String type) {
        CarDto deletedCar = carService.deleteCar(carId, type);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car deleted successfully (" + type + ")", deletedCar.getCarId()));
    }

    /** GET Cars by Seller & Status */
    @GetMapping("/getcarbyseller")
    public ResponseEntity<ApiResponse<Page<CarDto>>> getCarsBySellerAndStatus(
            @RequestParam Integer sellerId,
            @RequestParam Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CarDto> cars = carService.getCarsBySellerAndStatus(sellerId, status, page, size);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Cars retrieved successfully", cars));
    }

    /** GET Cars by Status */
    @GetMapping("/GetCarbyStatus")
    public ResponseEntity<ApiResponse<Page<CarDto>>> getCarsByStatus(@RequestParam Status status,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<CarDto> cars = carService.getCarsByStatus(status, page, size);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Cars retrieved successfully", cars));
    }

    /** Count Cars by Seller & Status */
    @GetMapping("/CarCount")
    public ResponseEntity<ApiResponse<Long>> countCarsBySellerAndStatus(@RequestParam Integer sellerId,
                                                                        @RequestParam Status status) {
        Long count = carService.countCarsBySellerAndStatus(sellerId, status);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car count fetched successfully", count));
    }

    /** GET Car by MainCarID */
//    @GetMapping("/GetCarByMainCarID")
//    public ResponseEntity<ApiResponse<CarDto>> getCarByMainCarId(@RequestParam String mainCarId) {
//        CarDto car = carService.getByMainCarId(mainCarId);
//        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car retrieved successfully", car));
//    }
}

