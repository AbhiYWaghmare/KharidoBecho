package com.spring.jwt.car.service;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // ------------------- Mapping methods -------------------
    private CarDto toDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setCarName(car.getTitle());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setVariant(car.getVariant());
        dto.setPrice(car.getPrice());
        dto.setColor(car.getColor());
        dto.setFuelType(car.getFuelType());
        dto.setKmDriven(car.getKmDriven());
        dto.setTransmission(car.getTransmission());
        dto.setYear(car.getYear());
        dto.setDate(car.getDate());
        dto.setDescription(car.getDescription());
        dto.setDealerId(car.getDealerId());
        dto.setBuyerId(car.getOwnerSerial());
        dto.setCarType(car.getCarType());
        dto.setCarStatus(car.getCarStatus());
        return dto;
    }

    private Car toEntity(CarDto dto) {
        Car car = new Car();
        car.setTitle(dto.getCarName());
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setVariant(dto.getVariant());
        car.setPrice(dto.getPrice() != null ? dto.getPrice() : 0);
        car.setColor(dto.getColor());
        car.setFuelType(dto.getFuelType());
        car.setKmDriven(dto.getKmDriven() != null ? dto.getKmDriven() : 0);
        car.setTransmission(dto.getTransmission());
        car.setYear(dto.getYear() != null ? dto.getYear() : 0);
        car.setDate(dto.getDate());
        car.setDescription(dto.getDescription());
        car.setDealerId(dto.getDealerId() != null ? dto.getDealerId() : 0);
        car.setOwnerSerial(dto.getBuyerId() != null ? dto.getBuyerId() : 0);
        car.setCarType(dto.getCarType());
        car.setCarStatus(dto.getCarStatus() != null ? dto.getCarStatus() : Status.ACTIVE);
        return car;
    }

    // ------------------- Car CRUD methods -------------------
    @Override
    public CarDto createCar(CarDto dto) {
        Car saved = carRepository.save(toEntity(dto));
        return toDto(saved);
    }

    @Override
    public CarDto patchUpdateCar(Integer carId, CarDto partialDto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found for id: " + carId));

        if (partialDto.getCarName() != null) car.setTitle(partialDto.getCarName());
        if (partialDto.getBrand() != null) car.setBrand(partialDto.getBrand());
        if (partialDto.getModel() != null) car.setModel(partialDto.getModel());
        if (partialDto.getVariant() != null) car.setVariant(partialDto.getVariant());
        if (partialDto.getPrice() != null) car.setPrice(partialDto.getPrice());
        if (partialDto.getColor() != null) car.setColor(partialDto.getColor());
        if (partialDto.getFuelType() != null) car.setFuelType(partialDto.getFuelType());
        if (partialDto.getKmDriven() != null) car.setKmDriven(partialDto.getKmDriven());
        if (partialDto.getTransmission() != null) car.setTransmission(partialDto.getTransmission());
        if (partialDto.getYear() != null) car.setYear(partialDto.getYear());
        if (partialDto.getDate() != null) car.setDate(partialDto.getDate());
        if (partialDto.getDescription() != null) car.setDescription(partialDto.getDescription());
        if (partialDto.getDealerId() != null) car.setDealerId(partialDto.getDealerId());
        if (partialDto.getBuyerId() != null) car.setOwnerSerial(partialDto.getBuyerId());
        if (partialDto.getCarType() != null) car.setCarType(partialDto.getCarType());
        if (partialDto.getCarStatus() != null) car.setCarStatus(partialDto.getCarStatus());

        return toDto(carRepository.save(car));
    }

    @Override
    public CarDto getCarById(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found for id: " + carId));
        return toDto(car);
    }

    @Override
    public CarDto deleteCar(Integer carId, String type) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found for id: " + carId));

        if ("soft".equalsIgnoreCase(type)) {
            car.setCarStatus(Status.DELETED);
            carRepository.save(car);
        } else if ("hard".equalsIgnoreCase(type)) {
            carRepository.delete(car);
        } else {
            throw new IllegalArgumentException("Invalid delete type: " + type);
        }

        return null;
    }

    @Override
    public Page<CarDto> getCarsBySellerAndStatus(Integer sellerId, Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return carRepository.findByDealerIdAndCarStatus(sellerId, status, pageable).map(this::toDto);
    }

    @Override
    public Page<CarDto> getCarsByStatus(Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return carRepository.findByCarStatus(status, pageable).map(this::toDto);
    }

    @Override
    public long countCarsBySellerAndStatus(Integer sellerId, Status status) {
        return carRepository.countByDealerIdAndCarStatus(sellerId, status);
    }
}
