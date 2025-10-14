package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.car.exception.CarAlreadyExists;
import com.spring.jwt.car.response.ApiResponse;
import com.spring.jwt.car.service.CarService;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//********************************************************//
// Author : Abhishek Patil
// Car Controller with Validation, Error Handling & Clear Response
// Date : 14/10/2025
//********************************************************//

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    //====================================================//
    //  CREATE CAR                                        //
    //  POST /cars/newcar                                 //
    //====================================================//
    @PostMapping("/newcar")
    public ResponseEntity<ApiResponse<Long>> createCar(@Valid @RequestBody CarDto dto) {

        // ✅ Prevent duplicate registration
        if (carService.findByRegistration(dto.getRegistration())) {
            throw new CarAlreadyExists(
                    "Car with registration number " + dto.getRegistration() + " already exists"
            );
        }

        // ✅ Create car
        CarDto created = carService.createCar(dto);

        // ✅ Return response with ID in message
        String message = String.format("Car created successfully with ID: %d", created.getCarId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("SUCCESS", message, created.getCarId()));
    }

    //====================================================//
    //  PATCH UPDATE CAR                                  //
    //  PATCH /cars/updatecar                             //
    //====================================================//
    @PatchMapping("/updatecar")
    public ResponseEntity<ApiResponse<Long>> updateCar(@RequestParam Long carId,
                                                       @Valid @RequestBody CarDto dto) {

        // ✅ Verify existence
        CarDto existingCar = carService.getCarById(carId);
        if (existingCar == null) {
            throw new ResourceNotFoundException("Car with ID " + carId + " not found");
        }

        // ✅ Update car
        CarDto updated = carService.patchUpdateCar(carId, dto);

        // ✅ Response with specific ID
        String message = String.format("Car updated successfully with ID: %d", updated.getCarId());
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", message, updated.getCarId()));
    }

    //====================================================//
    //  GET CAR BY ID                                     //
    //  GET /cars/getById                                 //
    //====================================================//
    @GetMapping("/getById")
    public ResponseEntity<CarDto> getCarById(@RequestParam Long carId) {
        CarDto car = carService.getCarById(carId);

        if (car == null) {
            throw new ResourceNotFoundException("Car with ID " + carId + " not found");
        }

        return ResponseEntity.ok(car);
    }

    //====================================================//
    //  DELETE CAR                                        //
    //  DELETE /cars/delete                               //
    //====================================================//
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> deleteCar(@RequestParam Long carId,
                                                       @RequestParam String type) {

        CarDto car = carService.getCarById(carId);
        if (car == null) {
            throw new ResourceNotFoundException("Car with ID " + carId + " not found");
        }

        carService.deleteCar(carId, type);

        String message = String.format("Car deleted successfully (ID: %d, Type: %s)", carId, type);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", message, carId));
    }

    //====================================================//
    //  GET CARS BY SELLER AND STATUS                     //
    //  GET /cars/getcarbyseller                          //
    //====================================================//
    @GetMapping("/getcarbyseller")
    public ResponseEntity<Page<CarDto>> getCarsBySellerAndStatus(
            @RequestParam Long sellerId,
            @RequestParam(name = "car_status") Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CarDto> cars = carService.getCarsBySellerAndStatus(sellerId, status, page, size);

        if (cars.isEmpty()) {
            throw new ResourceNotFoundException("No cars found for seller " + sellerId + " with status " + status);
        }

        return ResponseEntity.ok(cars);
    }

    //====================================================//
    //  GET CARS BY STATUS                                //
    //  GET /cars/getByStatus                             //
    //====================================================//
    @GetMapping("/getByStatus")
    public ResponseEntity<Page<CarDto>> getCarsByStatus(@RequestParam Status status,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {

        Page<CarDto> cars = carService.getCarsByStatus(status, page, size);

        if (cars.isEmpty()) {
            throw new ResourceNotFoundException("No cars found with status: " + status);
        }

        return ResponseEntity.ok(cars);
    }

    //====================================================//
    //  COUNT CARS BY SELLER AND STATUS                   //
    //  GET /cars/CarCount                                //
    //====================================================//
    @GetMapping("/CarCount")
    public ResponseEntity<ApiResponse<Long>> countCarsBySellerAndStatus(@RequestParam Long sellerId,
                                                                        @RequestParam Status status) {

        Long count = carService.countCarsBySellerAndStatus(sellerId, status);
        String message = String.format("Number of cars for seller %d with status %s = %d", sellerId, status, count);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", message, count));
    }
}
