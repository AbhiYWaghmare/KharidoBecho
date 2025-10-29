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
    Laptop update(Long laptopId, LaptopRequestDTO laptopRequestDTO);

    //Method to get laptop by id
    Laptop getById(Long laptopId);

    //Method to get all laptops
    List<Laptop> getAllLaptops();

    Page<Laptop> getBySellerIdAndStatus(Long sellerId, Status status, int page, int size, String sortBy);

    Page<Laptop> getByStatus(Status status, int page, int size, String sortBy);

    void deleteLaptopById(Long laptopId);

    Long countBySellerIdAndStatus(Long sellerId, Status status);


}
