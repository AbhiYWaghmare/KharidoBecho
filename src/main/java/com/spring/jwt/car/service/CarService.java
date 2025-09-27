package com.spring.jwt.car.service;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;

public interface CarService {
    CarDto createCar(CarDto dto);
    CarDto patchUpdateCar(Integer carId, CarDto partialDto);
    CarDto getCarById(Integer carId);
    CarDto deleteCar(Integer carId, String type);
    Page<CarDto> getCarsBySellerAndStatus(Integer sellerId, Status status, int page, int size);
    Page<CarDto> getCarsByStatus(Status status, int page, int size);
    long countCarsBySellerAndStatus(Integer sellerId, Status status);
}
