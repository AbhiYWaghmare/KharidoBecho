package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.laptop.BlankFieldsException;
import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.laptop.Dropdown.dto.LaptopBrandModelDTO;
import com.spring.jwt.laptop.Dropdown.model.*;
import com.spring.jwt.laptop.Dropdown.service.LaptopBrandModelService;
import com.spring.jwt.laptop.dto.LaptopBookingDTO;
import com.spring.jwt.laptop.dto.LaptopImageDTO;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
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
    private final LaptopBrandModelService laptopBrandModelService;

    public Laptop create(LaptopRequestDTO requestDTO) {

        Seller seller = sellerRepository.findById(requestDTO.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(requestDTO.getSellerId()));

        if (laptopRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
            throw new LaptopAlreadyExistsException(
                    "Laptop already exists with serial number " + requestDTO.getSerialNumber()
            );
        }

        Laptop laptop = new Laptop();

        laptop.setSerialNumber(requestDTO.getSerialNumber());
        laptop.setDealer(requestDTO.getDealer());
        laptop.setBrand(requestDTO.getBrand());
        laptop.setModel(requestDTO.getModel());

        laptop.setPrice(requestDTO.getPrice());
        laptop.setProcessor(requestDTO.getProcessor());
        laptop.setBattery(requestDTO.getBattery());
        laptop.setBatteryLife(requestDTO.getBatteryLife());
        laptop.setGraphicsCard(requestDTO.getGraphicsCard());
        laptop.setManufacturer(requestDTO.getManufacturer());
        laptop.setUsbPorts(requestDTO.getUsbPorts());
        laptop.setWeight(requestDTO.getWeight());
        laptop.setColour(requestDTO.getColour());
        laptop.setStatus(
                requestDTO.getStatus() != null
                        ? requestDTO.getStatus()
                        : Status.ACTIVE
        );

        laptop.setSeller(seller);
        if (requestDTO.getRam() != null)
            laptop.setRam(RamOption.fromDbValue(requestDTO.getRam()));

        if (requestDTO.getStorage() != null)
            laptop.setStorage(StorageOption.fromDbValue(requestDTO.getStorage()));

        if (requestDTO.getScreenSize() != null)
            laptop.setScreenSize(ScreenSize.fromDbValue(requestDTO.getScreenSize()));

        if (requestDTO.getMemoryType() != null)
            laptop.setMemoryType(MemoryType.fromDbValue(requestDTO.getMemoryType()));

        if (requestDTO.getProcessorBrand() != null)
            laptop.setProcessorBrand(
                    ProcessorBrand.fromDbValue(requestDTO.getProcessorBrand())
            );

        if (requestDTO.getGraphicBrand() != null)
            laptop.setGraphicBrand(
                    GraphicsBrand.fromDbValue(requestDTO.getGraphicBrand())
            );

        if (requestDTO.getWarrantyInYear() != null)
            laptop.setWarrantyInYear(
                    Warranty.fromYears(requestDTO.getWarrantyInYear())
            );

        laptopBrandModelService.add(
                LaptopBrandModelDTO.builder()
                        .brand(requestDTO.getBrand())
                        .model(requestDTO.getModel())
                        .build()
        );

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
                    .orElseThrow(() ->
                            new LaptopNotFoundException("Laptop not found with ID " + laptopId));

            if (laptopRequestDTO.getSerialNumber() != null
                    && !laptopRequestDTO.getSerialNumber().equals(laptop.getSerialNumber())) {

                if (laptopRepository.existsBySerialNumber(
                        laptopRequestDTO.getSerialNumber())) {
                    throw new LaptopAlreadyExistsException(
                            "Laptop with serial number " + laptopRequestDTO.getSerialNumber() + " already exists");
                }
                laptop.setSerialNumber(laptopRequestDTO.getSerialNumber());
            }

            if (laptopRequestDTO.getDealer() != null)
                laptop.setDealer(laptopRequestDTO.getDealer());
            if (laptopRequestDTO.getBrand() != null)
                laptop.setBrand(laptopRequestDTO.getBrand());

            if (laptopRequestDTO.getModel() != null)
                laptop.setModel(laptopRequestDTO.getModel());

            if (laptopRequestDTO.getPrice() != null) {
                if (laptopRequestDTO.getPrice() < 0) {
                    throw new IllegalArgumentException("Price must be non-negative");
                }
                laptop.setPrice(laptopRequestDTO.getPrice());
            }

            if (laptopRequestDTO.getProcessor() != null)
                laptop.setProcessor(laptopRequestDTO.getProcessor());

            if (laptopRequestDTO.getBattery() != null)
                laptop.setBattery(laptopRequestDTO.getBattery());

            if (laptopRequestDTO.getBatteryLife() != null)
                laptop.setBatteryLife(laptopRequestDTO.getBatteryLife());

            if (laptopRequestDTO.getGraphicsCard() != null)
                laptop.setGraphicsCard(laptopRequestDTO.getGraphicsCard());

            if (laptopRequestDTO.getWeight() != null)
                laptop.setWeight(laptopRequestDTO.getWeight());

            if (laptopRequestDTO.getManufacturer() != null)
                laptop.setManufacturer(laptopRequestDTO.getManufacturer());

            if (laptopRequestDTO.getUsbPorts() != null)
                laptop.setUsbPorts(laptopRequestDTO.getUsbPorts());

            if (laptopRequestDTO.getStatus() != null)
                laptop.setStatus(laptopRequestDTO.getStatus());

            if (laptopRequestDTO.getRam() != null)
                laptop.setRam(RamOption.fromDbValue(laptopRequestDTO.getRam()));

            if (laptopRequestDTO.getStorage() != null)
                laptop.setStorage(StorageOption.fromDbValue(laptopRequestDTO.getStorage()));

            if (laptopRequestDTO.getScreenSize() != null)
                laptop.setScreenSize(ScreenSize.fromDbValue(laptopRequestDTO.getScreenSize()));

            if (laptopRequestDTO.getMemoryType() != null)
                laptop.setMemoryType(MemoryType.fromDbValue(laptopRequestDTO.getMemoryType()));

            if (laptopRequestDTO.getProcessorBrand() != null)
                laptop.setProcessorBrand(
                        ProcessorBrand.fromDbValue(laptopRequestDTO.getProcessorBrand()));

            if (laptopRequestDTO.getGraphicBrand() != null)
                laptop.setGraphicBrand(
                        GraphicsBrand.fromDbValue(laptopRequestDTO.getGraphicBrand()));

            if (laptopRequestDTO.getWarrantyInYear() != null)
                laptop.setWarrantyInYear(
                        Warranty.fromYears(laptopRequestDTO.getWarrantyInYear()));

            if (laptopRequestDTO.getSellerId() != null) {
                Seller seller = sellerRepository.findById(laptopRequestDTO.getSellerId())
                        .orElseThrow(() ->
                                new SellerNotFoundException(laptopRequestDTO.getSellerId()));
                laptop.setSeller(seller);
            }

            return laptopRepository.save(laptop);
        }


    @Transactional(readOnly = true)
    public LaptopResponseDTO getById(Long laptopId) {

        Laptop laptop = laptopRepository.findByIdWithPhotos(laptopId);

        if (laptop == null) {
            throw new LaptopNotFoundException(
                    "Laptop with ID " + laptopId + " not found");
        }

        LaptopResponseDTO dto = new LaptopResponseDTO();

        dto.setId(laptop.getId());
        dto.setSerialNumber(laptop.getSerialNumber());
        dto.setBrand(laptop.getBrand());
        dto.setModel(laptop.getModel());

        dto.setPrice(laptop.getPrice());
        dto.setProcessor(laptop.getProcessor());
        dto.setBattery(laptop.getBattery());
        dto.setBatteryLife(laptop.getBatteryLife());
        dto.setGraphicsCard(laptop.getGraphicsCard());
        dto.setWeight(laptop.getWeight());
        dto.setManufacturer(laptop.getManufacturer());
        dto.setUsbPorts(laptop.getUsbPorts());
        dto.setStatus(laptop.getStatus());
        dto.setDeleted(laptop.isDeleted());
        dto.setDeletedAt(laptop.getDeletedAt());
        dto.setRam(
                laptop.getRam() != null ? laptop.getRam().getDbValue() : null
        );
        dto.setStorage(
                laptop.getStorage() != null ? laptop.getStorage().getDbValue() : null
        );
        dto.setScreenSize(
                laptop.getScreenSize() != null ? laptop.getScreenSize().getDbValue() : null
        );
        dto.setMemoryType(
                laptop.getMemoryType() != null ? laptop.getMemoryType().getDbValue() : null
        );
        dto.setProcessorBrand(
                laptop.getProcessorBrand() != null ? laptop.getProcessorBrand().getDbValue() : null
        );
        dto.setGraphicBrand(
                laptop.getGraphicBrand() != null ? laptop.getGraphicBrand().getDbValue() : null
        );
        dto.setWarrantyInYear(
                laptop.getWarrantyInYear() != null
                        ? Long.parseLong(laptop.getWarrantyInYear().getDbValue())
                        : null
        );

        dto.setPhotos(
                laptop.getLaptopPhotos() == null
                        ? List.of()
                        : laptop.getLaptopPhotos().stream().map(p -> {
                    LaptopImageDTO img = new LaptopImageDTO();
                    img.setPhotoId(p.getPhotoId());
                    img.setPhoto_link(p.getPhoto_link());
                    return img;
                }).toList()
        );

        return dto;
    }

@Transactional(readOnly = true)
public List<LaptopResponseDTO> getAllLaptops() {

    List<Laptop> laptops = laptopRepository.findAllWithPhotos();

    if (laptops.isEmpty()) {
        throw new LaptopNotFoundException("No laptops found");
    }

    return laptops.stream().map(laptop -> {

        LaptopResponseDTO dto = new LaptopResponseDTO();

        dto.setId(laptop.getId());
        dto.setSerialNumber(laptop.getSerialNumber());
        dto.setBrand(laptop.getBrand());
        dto.setModel(laptop.getModel());

        dto.setPrice(laptop.getPrice());
        dto.setProcessor(laptop.getProcessor());
        dto.setBattery(laptop.getBattery());
        dto.setBatteryLife(laptop.getBatteryLife());
        dto.setGraphicsCard(laptop.getGraphicsCard());
        dto.setWeight(laptop.getWeight());
        dto.setManufacturer(laptop.getManufacturer());
        dto.setUsbPorts(laptop.getUsbPorts());
        dto.setStatus(laptop.getStatus());
        dto.setDeleted(laptop.isDeleted());
        dto.setDeletedAt(laptop.getDeletedAt());
        dto.setRam(laptop.getRam() != null ? laptop.getRam().getDbValue() : null);
        dto.setStorage(laptop.getStorage() != null ? laptop.getStorage().getDbValue() : null);
        dto.setScreenSize(laptop.getScreenSize() != null ? laptop.getScreenSize().getDbValue() : null);
        dto.setMemoryType(laptop.getMemoryType() != null ? laptop.getMemoryType().getDbValue() : null);
        dto.setProcessorBrand(laptop.getProcessorBrand() != null ? laptop.getProcessorBrand().getDbValue() : null);
        dto.setGraphicBrand(laptop.getGraphicBrand() != null ? laptop.getGraphicBrand().getDbValue() : null);
        dto.setWarrantyInYear(
                laptop.getWarrantyInYear() != null
                        ? Long.parseLong(laptop.getWarrantyInYear().getDbValue())
                        : null
        );

        dto.setPhotos(
                laptop.getLaptopPhotos().stream().map(p -> {
                    LaptopImageDTO pd = new LaptopImageDTO();
                    pd.setPhotoId(p.getPhotoId());
                    pd.setPhoto_link(p.getPhoto_link());
                    return pd;
                }).toList()
        );

        dto.setBookings(
                laptop.getBookings().stream().map(b -> {
                    LaptopBookingDTO bd = new LaptopBookingDTO();
                    bd.setLaptopBookingId(b.getLaptopBookingId());
                    bd.setBuyerId(b.getBuyer().getBuyerId());
                    bd.setSellerId(b.getSeller().getSellerId());
                    bd.setStatus(b.getPendingStatus().name());
                    return bd;
                }).toList()
        );

        return dto;

    }).toList();
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
    @Transactional(readOnly = true)
    public Page<Laptop> getBySellerIdAndStatus(Long sellerId, Status status, int page, int size, String sortBy) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        Page<Laptop> laptopPage =
                laptopRepository.findBySellerIdAndStatus(sellerId, status, pageable);

        laptopPage.getContent().forEach(laptop -> {
            if (laptop.getLaptopPhotos() != null) {
                laptop.getLaptopPhotos().size();
            }
        });

        return laptopPage;

    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Page<Laptop> getAllBySellerId(Long sellerId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return laptopRepository.findBySeller_SellerId(sellerId, pageable);
    }


    @Transactional
    public LaptopResponseDTO updateLaptop(Long laptopId, LaptopRequestDTO laptopRequestDTO) {

        Laptop laptop = update(laptopId, laptopRequestDTO);

        LaptopResponseDTO dto = new LaptopResponseDTO();
        dto.setId(laptop.getId());
        dto.setSerialNumber(laptop.getSerialNumber());
        dto.setBrand(laptop.getBrand());
        dto.setModel(laptop.getModel());

        dto.setPrice(laptop.getPrice());
        dto.setStatus(laptop.getStatus());

        return dto;

    }
}
