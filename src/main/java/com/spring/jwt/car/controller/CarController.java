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

    // ✅ POST -> Standard Response
    @PostMapping("/newcar")
    // response entity used to sendback http status + body
    public ResponseEntity<ApiResponse<Integer>> createCar(@RequestBody CarDto dto) {
        CarDto created = carService.createCar(dto);
        //call carservice for actually perform delete it returns CarDto layer
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("SUCCESS", "Car created successfully", created.getCarId()));
    }

    // ✅ PATCH -> Standard Response
    //only update selected filelds (in this i can send only selected fileds)
    //in this if we use put we have to send entire car json every time(update entire entity with new one )
    @PatchMapping("/updatecar")
    public ResponseEntity<ApiResponse<Integer>> updateCar(@RequestParam Integer carId,
                                                          @RequestBody CarDto dto) {
        CarDto updated = carService.patchUpdateCar(carId, dto);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car updated successfully", updated.getCarId()));
    }

    // ✅ GET -> Raw JSON Response
    @GetMapping("/getById")
    public ResponseEntity<CarDto> getCarById(@RequestParam Integer carId) {
        CarDto car = carService.getCarById(carId);
        return ResponseEntity.ok(car);
    }

    // ✅ DELETE -> Standard Response
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Integer>> deleteCar(@RequestParam Integer carId,
                                                          @RequestParam String type) {
        CarDto deletedCar = carService.deleteCar(carId, type);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Car deleted successfully (" + type + ")", deletedCar.getCarId()));
    }

    // ✅ GET -> Raw JSON Response
    @GetMapping("/getcarbyseller")
    public ResponseEntity<Page<CarDto>> getCarsBySellerAndStatus(
            @RequestParam Integer sellerId,
            @RequestParam(name = "car_status") Status status,  // match request param name
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CarDto> cars = carService.getCarsBySellerAndStatus(sellerId,status, page, size);
        return ResponseEntity.ok(cars);
    }


    // ✅ GET -> Raw JSON Response
    @GetMapping("/getByStatus")
    public ResponseEntity<Page<CarDto>> getCarsByStatus(@RequestParam Status status,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<CarDto> cars = carService.getCarsByStatus(status, page, size);
        return ResponseEntity.ok(cars);
    }

    //  Raw JSON Response
    @GetMapping("/CarCount")
    public ResponseEntity<Long> countCarsBySellerAndStatus(@RequestParam Integer sellerId,
                                                           @RequestParam Status status) {
        Long count = carService.countCarsBySellerAndStatus(sellerId, status);
        return ResponseEntity.ok(count);
    }
}
