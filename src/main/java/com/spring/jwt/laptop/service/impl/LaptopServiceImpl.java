package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.laptop.BlankFieldsException;
import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
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
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final SellerRepository sellerRepository;

    public Laptop create(LaptopRequestDTO requestDTO) {

        Seller seller = sellerRepository.findById(requestDTO.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(requestDTO.getSellerId()));

        if(laptopRepository.existsBySerialNumber(requestDTO.getSerialNumber())){
            throw new LaptopAlreadyExistsException("Laptop already exists with serial number " + requestDTO.getSerialNumber());
        }

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
        laptop.setSeller(seller);

        return laptopRepository.save(laptop);
    }



    private void validateBlankFields(LaptopRequestDTO laptopRequestDTO) {
        Arrays.stream(laptopRequestDTO.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(String.class)) // only check string fields
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(laptopRequestDTO);
                        if (value != null && ((String) value).isBlank()) {
                            throw new BlankFieldsException(field.getName() + " cannot be blank");
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error accessing field: " + field.getName(), e);
                    }
                });
    }


    @Transactional
    public Laptop update(Long laptopId, LaptopRequestDTO laptopRequestDTO) {

        validateBlankFields(laptopRequestDTO);

        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with ID " + laptopId));

        // Only update fields if they are not null
        if (laptopRequestDTO.getSerialNumber() != null && !laptopRequestDTO.getSerialNumber().equals(laptop.getSerialNumber())) {
            if (laptopRepository.existsBySerialNumber(laptopRequestDTO.getSerialNumber())) {
                throw new LaptopAlreadyExistsException("Laptop with serial number " + laptopRequestDTO.getSerialNumber() + " already exists");
            }
            laptop.setSerialNumber(laptopRequestDTO.getSerialNumber());
        }
        if (laptopRequestDTO.getDealer() != null) laptop.setDealer(laptopRequestDTO.getDealer());
        if (laptopRequestDTO.getModel() != null) laptop.setModel(laptopRequestDTO.getModel());

        if (laptopRequestDTO.getBrand() != null) laptop.setBrand(laptopRequestDTO.getBrand());

        if (laptopRequestDTO.getPrice() != null) {
            if (laptopRequestDTO.getPrice() < 0) {
                throw new IllegalArgumentException("Price must be non-negative");
            }
            laptop.setPrice(laptopRequestDTO.getPrice());
        }

        if (laptopRequestDTO.getWarrantyInYear() != null) laptop.setWarrantyInYear(laptopRequestDTO.getWarrantyInYear());
        if (laptopRequestDTO.getProcessor() != null) laptop.setProcessor(laptopRequestDTO.getProcessor());
        if (laptopRequestDTO.getProcessorBrand() != null) laptop.setProcessorBrand(laptopRequestDTO.getProcessorBrand());
        if (laptopRequestDTO.getMemoryType() != null) laptop.setMemoryType(laptopRequestDTO.getMemoryType());
        if (laptopRequestDTO.getScreenSize() != null) laptop.setScreenSize(laptopRequestDTO.getScreenSize());
        if (laptopRequestDTO.getColour() != null) laptop.setColour(laptopRequestDTO.getColour());
        if(laptopRequestDTO.getRam() != null) laptop.setRam(laptopRequestDTO.getRam());
        if (laptopRequestDTO.getStorage() != null) laptop.setStorage(laptopRequestDTO.getStorage());
        if (laptopRequestDTO.getBattery() != null) laptop.setBattery(laptopRequestDTO.getBattery());
        if (laptopRequestDTO.getBatteryLife() != null) laptop.setBatteryLife(laptopRequestDTO.getBatteryLife());
        if (laptopRequestDTO.getGraphicsCard() != null) laptop.setGraphicsCard(laptopRequestDTO.getGraphicsCard());
        if (laptopRequestDTO.getGraphicBrand() != null) laptop.setGraphicBrand(laptopRequestDTO.getGraphicBrand());
        if (laptopRequestDTO.getWeight() != null) laptop.setWeight(laptopRequestDTO.getWeight());
        if (laptopRequestDTO.getManufacturer() != null) laptop.setManufacturer(laptopRequestDTO.getManufacturer());
        if (laptopRequestDTO.getUsbPorts() != null) laptop.setUsbPorts(laptopRequestDTO.getUsbPorts());
        if (laptopRequestDTO.getStatus() != null) laptop.setStatus(laptopRequestDTO.getStatus());

        if (laptopRequestDTO.getSellerId() != null) {
            Seller seller = sellerRepository.findById(laptopRequestDTO.getSellerId())
                    .orElseThrow(() -> new SellerNotFoundException(laptopRequestDTO.getSellerId()));
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
    @Transactional
    public String deleteLaptopById(Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with id: " + laptopId));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        log.debug("Processing delete for Laptop with ID {} at {}", laptopId, now);

        // === HARD DELETE (if already soft deleted) ===
        if (laptop.isDeleted() || laptop.getStatus() == Status.DELETE) {

            if (laptop.getBookings() != null && !laptop.getBookings().isEmpty()) {
                laptop.getBookings().clear();
            }

            if (laptop.getLaptopPhotos() != null && !laptop.getLaptopPhotos().isEmpty()) {
                laptop.getLaptopPhotos().clear();
            }

            // Now safe to hard delete
            laptopRepository.delete(laptop);
            log.info("Hard deleted Laptop with ID {}", laptopId);
            return "Laptop with ID " + laptopId + " was permanently deleted from database.";
        }

        // === SOFT DELETE (first delete call) ===
        laptop.setDeleted(true);
        laptop.setDeletedAt(now);
        laptop.setStatus(Status.DELETE);

        laptopRepository.save(laptop);
        log.info("Soft deleted Laptop with ID {} at {}", laptopId, now);

        return "Soft deleted Laptop with ID " + laptopId + " at " + now;
    }



    @Override
    public Page<Laptop> getBySellerIdAndStatus(Long sellerId, Status status, int page, int size, String sortBy) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));

        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).ascending());
        return laptopRepository.findBySellerIdAndStatus(sellerId,status,pageable);
    }

    @Override
    public Page<Laptop> getByStatus(Status status, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortBy).ascending());
        return laptopRepository.findByStatus(status, pageable);
    }


    @Override
    public Long countBySellerIdAndStatus(Long sellerId, Status status) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));

        return laptopRepository.countBySellerAndStatus(sellerId,status);
    }

    @Override
    public Page<Laptop> getAllBySellerId(Long sellerId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return laptopRepository.findBySeller_SellerId(sellerId, pageable);
    }


}
