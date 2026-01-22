package com.spring.jwt.car.carbranddata.service;

import com.spring.jwt.car.carbranddata.dto.CarBrandDataDto;
import com.spring.jwt.car.carbranddata.dto.CarOnlyBrandDto;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface CarBrandDataService {

    // Add brand should return the saved DTO (for ResponseDto in controller)
    CarBrandDataDto addBrand(CarBrandDataDto dto) throws SQLIntegrityConstraintViolationException;

    // Get all brands
    List<CarBrandDataDto> getAllBrands(Integer pageNo, Integer pageSize);

    // Edit brand
    String editBrand(Integer id, CarBrandDataDto dto);

    // Delete brand
    String deleteBrand(Integer id);

    // Get only brand names
    List<CarOnlyBrandDto> onlyBrands();

    // Get variants by brand
    List<CarBrandDataDto> variants(String brand);

    // Get sub-variants by brand and variant
    List<CarBrandDataDto> subVariant(String brand, String variant);
}
