package com.spring.jwt.laptop.service;


import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LaptopService {

    //Method to create new laptop
    Laptop create(LaptopRequestDTO laptopRequestDTO);

    //Method to update existing laptop
    Laptop update(Long id, String serialNumber, String dealer, String model, String brand, Double price);

    //Method to get laptop by id
    Laptop getById(Long id);

    //Method to get all laptops
    List<Laptop> getAllLaptops();

    //Method to delete by id
    void deleteById(Long id, String type);

    Page<Laptop> getByDealerIdAndStatus(Long dealerId, Status status, int page, int size, String sortBy);

    public Page<Laptop> getByStatus(Status status, int page, int size, String sortBy);

//    public long getLaptopCount(Long dealerId, String status);

}
