//package com.spring.jwt.car.service;
//
//import com.spring.jwt.car.dto.CarDto;
//import com.spring.jwt.car.exception.*;
//import com.spring.jwt.car.repository.CarRepository;
//import com.spring.jwt.entity.Car;
//import com.spring.jwt.entity.Seller;
//import com.spring.jwt.entity.Status;
//import com.spring.jwt.repository.SellerRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//@AllArgsConstructor
//public class CarServiceImpl implements CarService {
//
//    private final CarRepository carRepository;
//    private final SellerRepository sellerRepository;
//
//    // Mapping Car -> CarDto
//    private CarDto toDto(Car car) {
//        CarDto dto = new CarDto();
//        dto.setCarId(car.getCarId());
//        dto.setCarName(car.getTitle());
//        dto.setBrand(car.getBrand());
//        dto.setModel(car.getModel());
//        dto.setVariant(car.getVariant());
//        dto.setPrice(car.getPrice());
//        dto.setColor(car.getColor());
//        dto.setFuelType(car.getFuelType());
//        dto.setKmDriven(car.getKmDriven());
//        dto.setTransmission(car.getTransmission());
//        dto.setYear(car.getYear());
//        dto.setDate(car.getDate());
//        dto.setDescription(car.getDescription());
//        dto.setBuyerId(car.getOwnerSerial());
//        dto.setCarType(car.getCarType());
//        dto.setCarStatus(car.getCarStatus());
//        dto.setRegistration(car.getRegistration());
//        dto.setSellerId(car.getSeller() != null ? car.getSeller().getSellerId() : null);
//        dto.setCarPhotoId(car.getCarPhotoId());
//
//        // ✅ Additional fields
//        dto.setAirbag(car.getAirbag());
//        dto.setAbs(car.getABS());
//        dto.setButtonStart(car.getButtonStart());
//        dto.setSunroof(car.getSunroof());
//        dto.setChildSafetyLocks(car.getChildSafetyLocks());
//        dto.setAcFeature(car.getAcFeature());
//        dto.setMusicFeature(car.getMusicFeature());
//        dto.setArea(car.getArea());
//        dto.setCarInsurance(car.getCarInsurance());
//        dto.setCarInsuranceDate(car.getCarInsuranceDate());
//        dto.setCarInsuranceType(car.getCarInsuranceType());
//        dto.setPowerWindowFeature(car.getPowerWindowFeature());
//        dto.setRearParkingCameraFeature(car.getRearParkingCameraFeature());
//        dto.setCity(car.getCity());
//        dto.setPendingApproval(car.isPendingApproval());
//
//        return dto;
//    }
//
//    // Mapping CarDto -> Car
//    private Car toEntity(CarDto dto) {
//        Car car = new Car();
//        car.setTitle(dto.getCarName());
//        car.setBrand(dto.getBrand());
//        car.setModel(dto.getModel());
//        car.setVariant(dto.getVariant());
//        car.setPrice(dto.getPrice() != null ? dto.getPrice() : 0);
//        car.setColor(dto.getColor());
//        car.setFuelType(dto.getFuelType());
//        car.setKmDriven(dto.getKmDriven() != null ? dto.getKmDriven() : 0);
//        car.setTransmission(dto.getTransmission());
//        car.setYear(dto.getYear() != null ? dto.getYear() : 0);
//        car.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
//        car.setDescription(dto.getDescription());
//        car.setOwnerSerial(dto.getBuyerId() != null ? dto.getBuyerId() : 0);
//        car.setCarType(dto.getCarType());
//        car.setCarStatus(dto.getCarStatus() != null ? dto.getCarStatus() : Status.ACTIVE);
//        car.setRegistration(dto.getRegistration());
//        car.setCarPhotoId(dto.getCarPhotoId());
//
//        // ✅ Additional fields
//        car.setAirbag(dto.getAirbag());
//        car.setABS(dto.getAbs());
//        car.setButtonStart(dto.getButtonStart());
//        car.setSunroof(dto.getSunroof());
//        car.setChildSafetyLocks(dto.getChildSafetyLocks());
//        car.setAcFeature(dto.getAcFeature());
//        car.setMusicFeature(dto.getMusicFeature());
//        car.setArea(dto.getArea());
//        car.setCarInsurance(dto.getCarInsurance());
//        car.setCarInsuranceDate(dto.getCarInsuranceDate());
//        car.setCarInsuranceType(dto.getCarInsuranceType());
//        car.setPowerWindowFeature(dto.getPowerWindowFeature());
//        car.setRearParkingCameraFeature(dto.getRearParkingCameraFeature());
//        car.setCity(dto.getCity());
//        car.setPendingApproval(dto.getPendingApproval() != null ? dto.getPendingApproval() : false);
//
//        return car;
//    }
//    // CREATE CAR
//    @Override
//    public CarDto createCar(CarDto dto) {
//        Optional<Car> existingCar = carRepository.findByRegistration(dto.getRegistration());
//        if (existingCar.isPresent()) {
//            throw new CarAlreadyExists("Car with registration number " + dto.getRegistration() + " already exists.");
//        }
////        List<Car> cars = carRepository.findByRegistrationIsNull();
//
//
//        Car car = toEntity(dto);
//
//        Seller seller = sellerRepository.findById(dto.getSellerId())
//                .orElseThrow(() -> new SellerNotFoundException("Seller not found with ID: " + dto.getSellerId()));
//        car.setSeller(seller);
//
//        // Ensure car_photo_id is set if DB requires it
//        if (car.getCarPhotoId() == null) {
//            throw new InvalidRequestException("Car photo ID must be provided.");
//        }
//
//        Car savedCar = carRepository.save(car);
//        return toDto(savedCar);
//    }
//
//    // PATCH UPDATE CAR
//    @Override
//    public CarDto patchUpdateCar(Long carId, CarDto partialDto) {
//        Car car = carRepository.findById(carId)
//                .orElseThrow(() -> new CarNotFoundException("Car not found for id: " + carId));
//
//        if (partialDto.getSellerId() != null) {
//            Seller seller = sellerRepository.findById(partialDto.getSellerId())
//                    .orElseThrow(() -> new SellerNotFoundException("Seller not found with ID: " + partialDto.getSellerId()));
//            car.setSeller(seller);
//        }
//
//        if (partialDto.getCarStatus() != null) car.setCarStatus(partialDto.getCarStatus());
//        if (partialDto.getCarName() != null) car.setTitle(partialDto.getCarName());
//        if (partialDto.getBrand() != null) car.setBrand(partialDto.getBrand());
//        if (partialDto.getModel() != null) car.setModel(partialDto.getModel());
//        if (partialDto.getVariant() != null) car.setVariant(partialDto.getVariant());
//        if (partialDto.getPrice() != null) car.setPrice(partialDto.getPrice());
//        if (partialDto.getColor() != null) car.setColor(partialDto.getColor());
//        if (partialDto.getFuelType() != null) car.setFuelType(partialDto.getFuelType());
//        if (partialDto.getKmDriven() != null) car.setKmDriven(partialDto.getKmDriven());
//        if (partialDto.getTransmission() != null) car.setTransmission(partialDto.getTransmission());
//        if (partialDto.getYear() != null) car.setYear(partialDto.getYear());
//        if (partialDto.getDate() != null) car.setDate(partialDto.getDate());
//        if (partialDto.getDescription() != null) car.setDescription(partialDto.getDescription());
//        if (partialDto.getBuyerId() != null) car.setOwnerSerial(partialDto.getBuyerId());
//        if (partialDto.getCarType() != null) car.setCarType(partialDto.getCarType());
//        if (partialDto.getCarPhotoId() != null) car.setCarPhotoId(partialDto.getCarPhotoId());
//
//        return toDto(carRepository.save(car));
//    }
//
//    @Override
//    public CarDto getCarById(Long carId) {
//        Car car = carRepository.findById(carId)
//                .orElseThrow(() -> new EntityNotFoundException("Car not found for id: " + carId));
//        return toDto(car);
//    }
//
//    @Override
//    public CarDto deleteCar(Long carId, String type) {
//        Car car = carRepository.findById(carId)
//                .orElseThrow(() -> new CarNotFoundException("Car not found for id: " + carId));
//
//        if ("soft".equalsIgnoreCase(type)) {
//            car.setCarStatus(Status.DELETED);
//            carRepository.save(car);
//            return toDto(car);
//        } else if ("hard".equalsIgnoreCase(type)) {
//            CarDto deletedDto = toDto(car);
//            carRepository.delete(car);
//            return deletedDto;
//        } else {
//            throw new InvalidRequestException("Invalid delete type: " + type);
//        }
//    }
//
//    @Override
//    public Page<CarDto> getCarsBySellerAndStatus(Long sellerId, Status status, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("carId").descending());
//        return carRepository.findBySellerIdAndCarStatus(sellerId, status, pageable)
//                .map(this::toDto);
//    }
//
//    @Override
//    public Page<CarDto> getCarsByStatus(Status status, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("carId").descending());
//        return carRepository.findByCarStatus(status, pageable)
//                .map(this::toDto);
//    }
//
//    @Override
//    public long countCarsBySellerAndStatus(Long sellerId, Status status) {
//        return carRepository.countBySellerIdAndCarStatus(sellerId, status);
//    }
//}

package com.spring.jwt.car.service;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.car.exception.CarAlreadyExists;
import com.spring.jwt.car.exception.CarNotFoundException;
import com.spring.jwt.car.exception.InvalidRequestException;
import com.spring.jwt.car.exception.SellerNotFoundException;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import com.spring.jwt.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    // Mapping Car -> CarDto
    private CarDto toDto(Car car) {
        CarDto dto = new CarDto();
//        dto.setCarId(car.getCarId());
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
//        dto.setBuyerId(car.getOwnerSerial());
        dto.setCarType(car.getCarType());
        dto.setCarStatus(car.getCarStatus());
        dto.setRegistration(car.getRegistration());
        dto.setSellerId(car.getSeller() != null ? car.getSeller().getSellerId() : null);

        // ✅ Additional fields
        dto.setAirbag(car.getAirbag());
        dto.setAbs(car.getABS());
        dto.setButtonStart(car.getButtonStart());
        dto.setSunroof(car.getSunroof());
        dto.setChildSafetyLocks(car.getChildSafetyLocks());
        dto.setAcFeature(car.getAcFeature());
        dto.setMusicFeature(car.getMusicFeature());
        dto.setArea(car.getArea());
        dto.setCarInsurance(car.getCarInsurance());
        dto.setCarInsuranceDate(car.getCarInsuranceDate());
        dto.setCarInsuranceType(car.getCarInsuranceType());
        dto.setPowerWindowFeature(car.getPowerWindowFeature());
        dto.setRearParkingCameraFeature(car.getRearParkingCameraFeature());
        dto.setCity(car.getCity());
        dto.setPendingApproval(car.isPendingApproval());

        return dto;
    }

    // Mapping CarDto -> Car
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
        car.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        car.setDescription(dto.getDescription());
//        car.setOwnerSerial(dto.getBuyerId() != null ? dto.getBuyerId() : 0);
        car.setCarType(dto.getCarType());
        car.setCarStatus(dto.getCarStatus() != null ? dto.getCarStatus() : Status.ACTIVE);
        car.setRegistration(dto.getRegistration());

        // ✅ Additional fields
        car.setAirbag(dto.getAirbag());
        car.setABS(dto.getAbs());
        car.setButtonStart(dto.getButtonStart());
        car.setSunroof(dto.getSunroof());
        car.setChildSafetyLocks(dto.getChildSafetyLocks());
        car.setAcFeature(dto.getAcFeature());
        car.setMusicFeature(dto.getMusicFeature());
        car.setArea(dto.getArea());
        car.setCarInsurance(dto.getCarInsurance());
        car.setCarInsuranceDate(dto.getCarInsuranceDate());
        car.setCarInsuranceType(dto.getCarInsuranceType());
        car.setPowerWindowFeature(dto.getPowerWindowFeature());
        car.setRearParkingCameraFeature(dto.getRearParkingCameraFeature());
        car.setCity(dto.getCity());
        car.setPendingApproval(dto.getPendingApproval() != null ? dto.getPendingApproval() : false);

        return car;
    }

    // CREATE CAR
    @Override
    public CarDto createCar(CarDto dto) {
        Optional<Car> existingCar = carRepository.findByRegistration(dto.getRegistration());
        if (existingCar.isPresent()) {
            throw new CarAlreadyExists("Car with registration number " + dto.getRegistration() + " already exists.");
        }

        Car car = toEntity(dto);

        Seller seller = sellerRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException("Seller not found with ID: " + dto.getSellerId()));
        car.setSeller(seller);

        Car savedCar = carRepository.save(car);
        return toDto(savedCar);
    }

    // PATCH UPDATE CAR
    @Override
    public CarDto patchUpdateCar(Long carId, CarDto partialDto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found for id: " + carId));

        if (partialDto.getSellerId() != null) {
            Seller seller = sellerRepository.findById(partialDto.getSellerId())
                    .orElseThrow(() -> new SellerNotFoundException("Seller not found with ID: " + partialDto.getSellerId()));
            car.setSeller(seller);
        }

        if (partialDto.getCarStatus() != null) car.setCarStatus(partialDto.getCarStatus());
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
//        if (partialDto.getBuyerId() != null) car.setOwnerSerial(partialDto.getBuyerId());
        if (partialDto.getCarType() != null) car.setCarType(partialDto.getCarType());

        return toDto(carRepository.save(car));
    }

    @Override
    public CarDto getCarById(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found for id: " + carId));
        return toDto(car);
    }

    @Override
    public CarDto deleteCar(Long carId, String type) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found for id: " + carId));

        if ("soft".equalsIgnoreCase(type)) git{
            car.setCarStatus(Status.DELETED);
            carRepository.save(car);
            return toDto(car);
        } else if ("hard".equalsIgnoreCase(type)) {
            CarDto deletedDto = toDto(car);
            carRepository.delete(car);
            return deletedDto;
        } else {
            throw new InvalidRequestException("Invalid delete type: " + type);
        }
    }

    @Override
    public Page<CarDto> getCarsBySellerAndStatus(Long sellerId, Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("carId").descending());
        return carRepository.findBySellerIdAndCarStatus(sellerId, status, pageable)
                .map(this::toDto);
    }

    @Override
    public Page<CarDto> getCarsByStatus(Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("carId").descending());
        return carRepository.findByCarStatus(status, pageable)
                .map(this::toDto);
    }

    @Override
    public long countCarsBySellerAndStatus(Long sellerId, Status status) {
        return carRepository.countBySellerIdAndCarStatus(sellerId, status);
    }

    @Override
    public boolean findByRegistration(String registration) {
        return false;
    }
}

