package com.spring.jwt.car.service;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;

public interface CarService {
    CarDto createCar(CarDto dto);
    CarDto patchUpdateCar(Long carId, CarDto partialDto);
    CarDto getCarById(Long carId);
    CarDto deleteCar(Long carId, String type);
    Page<CarDto> getCarsBySellerAndStatus(Long sellerId, Status status, int page, int size);
    Page<CarDto> getCarsByStatus(Status status, int page, int size);
    long countCarsBySellerAndStatus(Long sellerId, Status status);
}
