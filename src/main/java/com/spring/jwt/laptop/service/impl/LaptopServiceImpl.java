package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.exceptionHandling.ResourceNotFoundException;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;

    public Laptop create(LaptopRequestDTO requestDTO) {
        Laptop laptop = new Laptop();
        laptop.setSerialNumber(requestDTO.getSerialNumber());
        laptop.setDealer(requestDTO.getDealer());
        laptop.setModel(requestDTO.getModel());
        laptop.setBrand(requestDTO.getBrand());
        laptop.setPrice(requestDTO.getPrice());
        return laptopRepository.save(laptop);
    }

    public Laptop update(Long id, String serialNumber, String dealer, String model, String brand, Double price) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop with ID " +id+ " not found!"));

        if (serialNumber != null && !serialNumber.isBlank()){
            laptop.setSerialNumber(serialNumber);
        }
        if (dealer != null && !dealer.isBlank()) {
            laptop.setDealer(dealer);
        }
        if (model != null && !model.isBlank()) {
            laptop.setModel(model);
        }
        if (brand != null && !brand.isBlank()) {
            laptop.setBrand(brand);
        }
        if (price != null && price > 0){
            laptop.setPrice(price);
        }

        return laptopRepository.save(laptop);
    }

    public Laptop getById(Long id) {
        return laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop with ID " + id + " not found"));
    }

    public List<Laptop> getAllLaptops() {

        return laptopRepository.findAll();
    }

    @Override
    public void deleteById(Long id, String type) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop with ID " +id + " not found"));

        if ("soft".equalsIgnoreCase(type)) {
            laptop.setStatus(Status.DELETED);
            laptopRepository.save(laptop);  // update instead of delete
        } else if ("hard".equalsIgnoreCase(type)) {
            laptopRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Invalid delete type: must be 'soft' or 'hard'");
        }
    }

    @Override
    public Page<Laptop> getByDealerIdAndStatus(Long dealerId, Status status, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).ascending());
        return laptopRepository.findBySellerIdAndStatus(dealerId,status,pageable);
    }

    @Override
    public Page<Laptop> getByStatus(Status status, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortBy).ascending());
        return laptopRepository.findByStatus(status, pageable);
    }

//    @Override
//    public long getLaptopCount(Long dealerId, String status) {
//        return laptopRepository.countByDealerIdAndStatus(dealerId,status);
//    }


}
