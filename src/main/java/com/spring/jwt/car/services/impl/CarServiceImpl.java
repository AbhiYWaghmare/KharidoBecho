package com.spring.jwt.car.services.impl;

import com.spring.jwt.car.dto.CarRequestDTO;
import com.spring.jwt.car.dto.CarResponseDTO;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.mapper.CarMapper;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.car.CarNotFoundException;
import com.spring.jwt.exception.car.CarValidationException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.repository.SellerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
//import com.spring.jwt.car.mapper.CarMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements com.spring.jwt.car.services.CarService {

    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    private void validateCreateRequest(CarRequestDTO req) {
        validateCommonFields(req, true);
    }

    private void validateUpdateRequest(CarRequestDTO req) {
        if (req.getTitle() == null && req.getDescription() == null && req.getPrice() == null &&
                req.getCondition() == null && req.getBrand() == null && req.getModel() == null &&
                req.getYearOfPurchase() == null && req.getSellerId() == null) {
            throw new CarValidationException("Update request body cannot be empty.");
        }
        validateCommonFields(req, false);
    }

    private void validateCommonFields(CarRequestDTO req, boolean isCreate) {
        // Seller check
        if (isCreate || req.getSellerId() !=  null) {
            Seller seller = sellerRepository.findById(req.getSellerId())
                    .orElseThrow(() -> new SellerNotFoundException(req.getSellerId()));

            if (Boolean.TRUE.equals(seller.isDeleted())) {
                throw new CarValidationException("Seller is deleted or inactive.");
            }
        }

        // Year check
        if (req.getYearOfPurchase() != null) {
            int currentYear = Year.now().getValue();
            if (req.getYearOfPurchase() > currentYear) {
                throw new CarValidationException("Year of Purchase cannot be in the future.");
            }
            if (req.getYearOfPurchase() < 2000) {
                throw new CarValidationException("Year must be after 2000.");
            }
        }

        // Price check
        if (req.getPrice() != null) {
            BigDecimal price = req.getPrice();
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new CarValidationException("Price must be greater than zero.");
            }
            if (price.compareTo(BigDecimal.valueOf(10_000_000)) > 0) {
                throw new CarValidationException("Price cannot exceed 1 crore.");
            }
            req.setPrice(price.setScale(2, RoundingMode.HALF_UP));
        }

        // Condition check
        if (req.getCondition() != null) {
            try {
                Car.Condition.valueOf(req.getCondition().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CarValidationException("Invalid condition. Use NEW, USED, or REFURBISHED.");
            }
        }

        // Duplicate check
        if (isCreate && carRepository.existsByTitleAndSeller_SellerId(req.getTitle(), req.getSellerId())) {
            throw new CarValidationException("Duplicate listing: same title already exists for this seller.");
        }

        // Title length check
        if (req.getTitle() != null && req.getTitle().length() > 150) {
            throw new CarValidationException("Title too long. Max 150 characters allowed.");
        }

        // Description check
        if (req.getDescription() != null) {
            int words = req.getDescription().trim().split("\\s+").length;
            if (words < 5) throw new CarValidationException("Description must have at least 5 words.");
            if (words > 70) throw new CarValidationException("Description cannot exceed 70 words.");
        }
    }

    @Override
    @Transactional
    public CarResponseDTO createCar(CarRequestDTO req) {
        validateCreateRequest(req);

        Seller seller = sellerRepository.findById(req.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(req.getSellerId()));

        Car car = new Car();
        CarMapper.updateFromRequest(car, req);
        car.setSeller(seller);
        car.setDeleted(false);
        car.setStatus(Car.Status.ACTIVE);
        car = carRepository.save(car);
        return com.spring.jwt.car.mapper.CarMapper.toDTO(car);
    }

    @Override
    public Page<CarResponseDTO> listCars(int page, int size, Long sellerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Car> pageResult = (sellerId != null)
                ? carRepository.findBySeller_SellerIdAndDeletedFalse(sellerId, pageable)
                : carRepository.findByDeletedFalse(pageable);

        return pageResult.map(CarMapper::toDTO);
    }


    @Override
    public CarResponseDTO getCar(Long id) {
        Car car = carRepository.findByCarIdAndDeletedFalse(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return com.spring.jwt.car.mapper.CarMapper.toDTO(car);
    }

    @Override
    @Transactional
    public CarResponseDTO updateCar(Long id, CarRequestDTO req) {
        validateUpdateRequest(req);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        if (car.isDeleted()) {
            throw new CarValidationException("Cannot update a deleted car.");
        }

        com.spring.jwt.car.mapper.CarMapper.updateFromRequest(car, req);
        car = carRepository.save(car);
        return com.spring.jwt.car.mapper.CarMapper.toDTO(car);
    }

    @Override
    @Transactional
    public void softDeleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.setDeleted(true);
        car.setDeletedAt(OffsetDateTime.now());
        car.setStatus(Car.Status.DELETED);
        carRepository.save(car);
    }
}
