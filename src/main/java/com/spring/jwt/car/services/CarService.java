package com.spring.jwt.car.services;

import com.spring.jwt.car.dto.CarRequestDTO;
import com.spring.jwt.car.dto.CarResponseDTO;
import org.springframework.data.domain.Page;

public interface CarService {
    CarResponseDTO createCar(CarRequestDTO request);
    Page<CarResponseDTO> listCars(int page, int size, Long sellerId);
    CarResponseDTO getCar(Long id);
    CarResponseDTO updateCar(Long id, CarRequestDTO request);
    void softDeleteCar(Long id);
}
