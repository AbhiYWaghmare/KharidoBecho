package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarCreateResponseDTO;
import com.spring.jwt.car.dto.CarRequestDTO;
import com.spring.jwt.car.dto.CarResponseDTO;
import com.spring.jwt.car.services.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//********************************************************//
// Author : Abhishek Waghmare
// Car Controller
// Date   : 15/10/2025
//********************************************************//

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    //  Add car for sell

    @PostMapping("/add")
    public ResponseEntity<CarCreateResponseDTO> createCar(@Valid @RequestBody CarRequestDTO request) {
        // normalize enums to uppercase
        request.setCondition(request.getCondition().toUpperCase());
        request.setFuelType(request.getFuelType().toUpperCase());
        request.setTransmission(request.getTransmission().toUpperCase());

        CarResponseDTO savedCar = carService.createCar(request);

        CarCreateResponseDTO response = CarCreateResponseDTO.builder()
                .code("201")
                .message("Car Added Successfully !!")
                .carId(savedCar.getCarId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all cars
    @GetMapping("/getAllCars")
    public ResponseEntity<Page<CarResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long sellerId
    ) {
        Page<CarResponseDTO> cars = carService.listCars(page, size, sellerId);
        return ResponseEntity.ok(cars);
    }

    // Get car by ID
    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCar(id));
    }

    // Update car by ID
    @PatchMapping("/update/{id}")
    public ResponseEntity<CarResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CarRequestDTO request) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    // Soft delete car by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        carService.softDeleteCar(id);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Car soft-deleted"));
    }
}
