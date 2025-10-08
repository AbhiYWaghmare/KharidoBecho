package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.car.response.ApiResponse;
import com.spring.jwt.car.service.CarService;
import com.spring.jwt.entity.Status;
import jakarta.validation.Valid;
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

    // CREATE CAR
    @PostMapping("/newcar")
    public ResponseEntity<ApiResponse<Long>> createCar(@Valid @RequestBody CarDto dto) {
        CarDto created = carService.createCar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("SUCCESS", "Car created successfully", created.getCarId()));
    }

    // PATCH UPDATE CAR
    @PatchMapping("/updatecar")
    public ResponseEntity<ApiResponse<Long>> updateCar(@RequestParam Long carId,
                                                       @RequestBody CarDto dto) {
        CarDto updated = carService.patchUpdateCar(carId, dto);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car updated successfully", updated.getCarId()));
    }

    // GET CAR BY ID
    @GetMapping("/getById")
    public ResponseEntity<CarDto> getCarById(@RequestParam Long carId) {
        CarDto car = carService.getCarById(carId);
        return ResponseEntity.ok(car);
    }

    // DELETE CAR
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> deleteCar(@RequestParam Long carId,
                                                       @RequestParam String type) {
        CarDto deletedCar = carService.deleteCar(carId, type);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car deleted successfully (" + type + ")", deletedCar.getCarId()));
    }

    // GET CARS BY SELLER AND STATUS
    @GetMapping("/getcarbyseller")
    public ResponseEntity<Page<CarDto>> getCarsBySellerAndStatus(
            @RequestParam Long sellerId,
            @RequestParam(name = "car_status") Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CarDto> cars = carService.getCarsBySellerAndStatus(sellerId, status, page, size);
        return ResponseEntity.ok(cars);
    }

    // GET CARS BY STATUS
    @GetMapping("/getByStatus")
    public ResponseEntity<Page<CarDto>> getCarsByStatus(@RequestParam Status status,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<CarDto> cars = carService.getCarsByStatus(status, page, size);
        return ResponseEntity.ok(cars);
    }

    // COUNT CARS BY SELLER AND STATUS
    @GetMapping("/CarCount")
    public ResponseEntity<Long> countCarsBySellerAndStatus(@RequestParam Long sellerId,
                                                           @RequestParam Status status) {
        Long count = carService.countCarsBySellerAndStatus(sellerId, status);
        return ResponseEntity.ok(count);
    }
}
