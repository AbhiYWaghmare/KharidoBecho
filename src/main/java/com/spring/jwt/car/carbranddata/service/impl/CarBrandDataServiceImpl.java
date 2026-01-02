package com.spring.jwt.car.carbranddata.service.impl;

import com.spring.jwt.car.carbranddata.dto.CarAllBrandDataDto;
import com.spring.jwt.car.carbranddata.dto.CarBrandDataDto;
import com.spring.jwt.car.carbranddata.dto.CarOnlyBrandDto;
import com.spring.jwt.car.carbranddata.entity.CarBrandData;
import com.spring.jwt.car.carbranddata.exception.CarBrandNotFoundException;
import com.spring.jwt.car.carbranddata.repository.CarBrandDataRepository;
import com.spring.jwt.car.carbranddata.service.CarBrandDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarBrandDataServiceImpl implements CarBrandDataService {

    @Autowired
    private CarBrandDataRepository carBrandDataRepository;

    // ================= ADD BRAND =================
    @Override
    public CarBrandDataDto addBrand(CarBrandDataDto dto)
            throws SQLIntegrityConstraintViolationException {

        Optional<CarBrandData> existingBrand =
                carBrandDataRepository.findByBrandAndVariantAndSubVariant(
                        dto.getBrand(),
                        dto.getVariant(),
                        dto.getSubVariant()
                );

        if (existingBrand.isPresent()) {
            throw new SQLIntegrityConstraintViolationException(
                    "Brand with same brand, variant and sub-variant already exists"
            );
        }

        CarBrandData brandData = new CarBrandData();
        brandData.setBrand(dto.getBrand());
        brandData.setVariant(dto.getVariant());
        brandData.setSubVariant(dto.getSubVariant());

        CarBrandData saved = carBrandDataRepository.save(brandData);
        return convertToDto(saved);
    }

    // ================= GET ALL BRANDS =================
    @Override
    public List<CarBrandDataDto> getAllBrands(Integer pageNo, Integer pageSize) {

        Pageable pageable =
                PageRequest.of(pageNo, pageSize, Sort.by("brandDataId").descending());

        Page<CarBrandData> brandPage =
                carBrandDataRepository.findAll(pageable);

        if (brandPage.isEmpty()) {
            throw new CarBrandNotFoundException("Brand not found");
        }

        return brandPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= EDIT BRAND =================
    @Override
    public String editBrand(Integer id, CarBrandDataDto dto) {

        CarBrandData brandData = carBrandDataRepository.findById(id)
                .orElseThrow(() ->
                        new CarBrandNotFoundException(
                                "Brand with ID " + id + " not found")
                );

        if (dto.getBrand() != null)
            brandData.setBrand(dto.getBrand());

        if (dto.getVariant() != null)
            brandData.setVariant(dto.getVariant());

        if (dto.getSubVariant() != null)
            brandData.setSubVariant(dto.getSubVariant());

        carBrandDataRepository.save(brandData);
        return "Brand updated successfully";
    }

    // ================= DELETE BRAND =================
    @Override
    public String deleteBrand(Integer id) {

        CarBrandData brandData = carBrandDataRepository.findById(id)
                .orElseThrow(() ->
                        new CarBrandNotFoundException(
                                "Brand with ID " + id + " not found")
                );

        carBrandDataRepository.delete(brandData);
        return "Brand deleted successfully";
    }

    // ================= ONLY BRANDS =================
    @Override
    public List<CarOnlyBrandDto> onlyBrands() {

        List<String> brands =
                carBrandDataRepository.findDistinctBrands();

        return brands.stream()
                .map(CarOnlyBrandDto::new)
                .collect(Collectors.toList());
    }

    // ================= VARIANTS =================
    @Override
    public List<CarBrandDataDto> variants(String brand) {

        List<CarBrandData> variants =
                carBrandDataRepository.findByBrand(brand);

        if (variants.isEmpty()) {
            throw new CarBrandNotFoundException(
                    "No variants found for brand " + brand
            );
        }

        return variants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= SUB VARIANTS =================
    @Override
    public List<CarBrandDataDto> subVariant(String brand, String variant) {

        List<CarBrandData> subVariants =
                carBrandDataRepository.findByBrandAndVariant(brand, variant);

        if (subVariants.isEmpty()) {
            throw new CarBrandNotFoundException(
                    "No sub-variants found for brand "
                            + brand + " and variant " + variant
            );
        }

        return subVariants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= DTO CONVERTER =================
    private CarBrandDataDto convertToDto(CarBrandData brandData) {

        CarBrandDataDto dto = new CarBrandDataDto();
        dto.setBrandDataId(brandData.getBrandDataId());
        dto.setBrand(brandData.getBrand());
        dto.setVariant(brandData.getVariant());
        dto.setSubVariant(brandData.getSubVariant());
        return dto;
    }
}
