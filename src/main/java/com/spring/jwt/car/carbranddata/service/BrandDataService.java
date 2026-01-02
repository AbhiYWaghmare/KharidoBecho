package com.spring.jwt.car.carbranddata.service;

import com.spring.jwt.car.carbranddata.dto.BrandDataDto;
import com.spring.jwt.car.carbranddata.dto.OnlyBrandDto;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface BrandDataService {

    // Add brand should return the saved DTO (for ResponseDto in controller)
    BrandDataDto addBrand(BrandDataDto dto) throws SQLIntegrityConstraintViolationException;

    // Get all brands
    List<BrandDataDto> getAllBrands(Integer pageNo, Integer pageSize);

    // Edit brand
    String editBrand(Integer id, BrandDataDto dto);

    // Delete brand
    String deleteBrand(Integer id);

    // Get only brand names
    List<OnlyBrandDto> onlyBrands();

    // Get variants by brand
    List<BrandDataDto> variants(String brand);

    // Get sub-variants by brand and variant
    List<BrandDataDto> subVariant(String brand, String variant);
}
