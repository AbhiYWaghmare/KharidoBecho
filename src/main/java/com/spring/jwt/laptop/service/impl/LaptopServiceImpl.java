package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.exceptionHandling.ResourceNotFoundException;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopRepository;

import java.util.List;

public class LaptopServiceImpl {

    private LaptopRepository laptopRepository;

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


    public void deleteLaptop(Long id, String type) {
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
}
