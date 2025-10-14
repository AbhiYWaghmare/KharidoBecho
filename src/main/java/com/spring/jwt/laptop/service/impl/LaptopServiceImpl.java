package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.laptop.service.LaptopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final SellerRepository sellerRepository;

    public Laptop create(LaptopRequestDTO requestDTO) {
//        if (requestDTO.getSerialNumber() == null || requestDTO.getSerialNumber().isBlank()) {
//            throw new IllegalArgumentException("Serial number is required");
//        }
//        if (requestDTO.getDealer() == null || requestDTO.getDealer().isBlank()) {
//            throw new IllegalArgumentException("Dealer is required");
//        }
//        if (requestDTO.getModel() == null || requestDTO.getModel().isBlank()) {
//            throw new IllegalArgumentException("Model is required");
//        }
//        if (requestDTO.getBrand() == null || requestDTO.getBrand().isBlank()) {
//            throw new IllegalArgumentException("Brand is required");
//        }
//        if (requestDTO.getPrice() == null || requestDTO.getPrice() < 0) {
//            throw new IllegalArgumentException("Price must be positive");
//        }
//        if (requestDTO.getSellerId() == null) {
//            throw new IllegalArgumentException("SellerId is required");
//        }

//        if (laptopRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
//            throw new LaptopAlreadyExistsException(
//                    "Laptop with serial number " + requestDTO.getSerialNumber() + " already exists"
//            );
//        }

        Laptop laptop = new Laptop();
        laptop.setSerialNumber(requestDTO.getSerialNumber());
        laptop.setDealer(requestDTO.getDealer());
        laptop.setModel(requestDTO.getModel());
        laptop.setBrand(requestDTO.getBrand());
        laptop.setPrice(requestDTO.getPrice());
        laptop.setWarrantyInYear(requestDTO.getWarrantyInYear());
        laptop.setProcessor(requestDTO.getProcessor());
        laptop.setStatus(requestDTO.getStatus());
        laptop.setBattery(requestDTO.getBattery());
        laptop.setBatteryLife(requestDTO.getBatteryLife());
        laptop.setColour(requestDTO.getColour());
        laptop.setGraphicBrand(requestDTO.getGraphicBrand());
        laptop.setGraphicsCard(requestDTO.getGraphicsCard());
        laptop.setManufacturer(requestDTO.getManufacturer());
        laptop.setMemoryType(requestDTO.getMemoryType());
        laptop.setProcessorBrand(requestDTO.getProcessorBrand());
        laptop.setRam(requestDTO.getRam());
        laptop.setScreenSize(requestDTO.getScreenSize());
        laptop.setStorage(requestDTO.getStorage());
        laptop.setUsbPorts(requestDTO.getUsbPorts());
        laptop.setWeight(requestDTO.getWeight());

        Seller seller = sellerRepository.findById(requestDTO.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(requestDTO.getSellerId()));
        laptop.setSeller(seller);

        return laptopRepository.save(laptop);
    }

    @Transactional
    public Laptop update(Long laptopId, LaptopRequestDTO dto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with ID " + laptopId));

        // Only update fields if they are not null
        if (dto.getSerialNumber() != null && !dto.getSerialNumber().equals(laptop.getSerialNumber())) {
            if (laptopRepository.existsBySerialNumber(dto.getSerialNumber())) {
                throw new LaptopAlreadyExistsException("Laptop with serial number " + dto.getSerialNumber() + " already exists");
            }
            laptop.setSerialNumber(dto.getSerialNumber());
        }

        if (dto.getDealer() != null) laptop.setDealer(dto.getDealer());
        if (dto.getModel() != null) laptop.setModel(dto.getModel());
        if (dto.getBrand() != null) laptop.setBrand(dto.getBrand());

        if (dto.getPrice() != null) {
            if (dto.getPrice() < 0) {
                throw new IllegalArgumentException("Price must be non-negative");
            }
            laptop.setPrice(dto.getPrice());
        }

        if (dto.getWarrantyInYear() != null) laptop.setWarrantyInYear(dto.getWarrantyInYear());
        if (dto.getProcessor() != null) laptop.setProcessor(dto.getProcessor());
        if (dto.getProcessorBrand() != null) laptop.setProcessorBrand(dto.getProcessorBrand());
        if (dto.getMemoryType() != null) laptop.setMemoryType(dto.getMemoryType());
        if (dto.getScreenSize() != null) laptop.setScreenSize(dto.getScreenSize());
        if (dto.getColour() != null) laptop.setColour(dto.getColour());
        if(dto.getRam() != null) laptop.setRam(dto.getRam());
        if (dto.getStorage() != null) laptop.setStorage(dto.getStorage());
        if (dto.getBattery() != null) laptop.setBattery(dto.getBattery());
        if (dto.getBatteryLife() != null) laptop.setBatteryLife(dto.getBatteryLife());
        if (dto.getGraphicsCard() != null) laptop.setGraphicsCard(dto.getGraphicsCard());
        if (dto.getGraphicBrand() != null) laptop.setGraphicBrand(dto.getGraphicBrand());
        if (dto.getWeight() != null) laptop.setWeight(dto.getWeight());
        if (dto.getManufacturer() != null) laptop.setManufacturer(dto.getManufacturer());
        if (dto.getUsbPorts() != null) laptop.setUsbPorts(dto.getUsbPorts());
        if (dto.getStatus() != null) laptop.setStatus(dto.getStatus());

        if (dto.getSellerId() != null) {
            Seller seller = sellerRepository.findById(dto.getSellerId())
                    .orElseThrow(() -> new SellerNotFoundException(dto.getSellerId()));
            laptop.setSeller(seller);
        }

        return laptopRepository.save(laptop);
    }


    public Laptop getById(Long laptopId) {
        return laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop with ID " + laptopId + " not found"));
    }

    public List<Laptop> getAllLaptops() {

        return laptopRepository.findAll();
    }


    @Override
    public void deleteLaptopById(Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with id: " + laptopId));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        log.debug("Processing delete for Laptop with ID {} at {}", laptopId, now);

        if (laptop.isDeleted() || laptop.getStatus() == Status.DELETED) {
            log.warn("Laptop with ID {} is already soft deleted", laptopId);

            laptopRepository.delete(laptop);
            log.info("Hard deleted Laptop with ID {}", laptopId);
        } else {
            laptop.setDeleted(true);
            laptop.setDeletedAt(now);
            laptop.setStatus(Status.DELETED);

            laptopRepository.save(laptop);
            log.info("Soft deleted Laptop with ID {} at {}", laptopId, now);
        }


    }

    @Override
    public Page<Laptop> getByDealerIdAndStatus(Long sellerId, Status status, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).ascending());
        return laptopRepository.findBySellerAndStatus(sellerId,status,pageable);
    }

    @Override
    public Page<Laptop> getByStatus(Status status, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortBy).ascending());
        return laptopRepository.findByStatus(status, pageable);
    }


    @Override
    public Long countBySellerIdAndStatus(Long sellerId, Status status) {
        return laptopRepository.countBySellerAndStatus(sellerId,status);
    }


}
