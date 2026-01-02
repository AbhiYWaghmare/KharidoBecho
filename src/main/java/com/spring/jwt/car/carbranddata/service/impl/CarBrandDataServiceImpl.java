package com.spring.jwt.car.carbranddata.service.impl;

import com.spring.jwt.car.carbranddata.dto.BrandDataDto;
import com.spring.jwt.car.carbranddata.dto.OnlyBrandDto;
import com.spring.jwt.car.carbranddata.entity.BrandData;
import com.spring.jwt.car.carbranddata.exception.BrandNotFoundException;
import com.spring.jwt.car.carbranddata.repository.BrandDataRepository;
import com.spring.jwt.car.carbranddata.service.BrandDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandDataServiceImpl implements BrandDataService {

    @Autowired
    private BrandDataRepository brandDataRepository;

    // ================= ADD BRAND =================
    @Override
    public BrandDataDto addBrand(BrandDataDto dto) throws SQLIntegrityConstraintViolationException {

        Optional<BrandData> existingBrand =
                brandDataRepository.findByBrandAndVariantAndSubVariant(
                        dto.getBrand(),
                        dto.getVariant(),
                        dto.getSubVariant()
                );

        if (existingBrand.isPresent()) {
            throw new SQLIntegrityConstraintViolationException(
                    "Brand with same brand, variant and sub-variant already exists"
            );
        }

        BrandData brandData = new BrandData();
        brandData.setBrand(dto.getBrand());
        brandData.setVariant(dto.getVariant());
        brandData.setSubVariant(dto.getSubVariant());

        BrandData saved = brandDataRepository.save(brandData);

        // return DTO for ResponseDto
        return convertToDto(saved);
    }

    // ================= GET ALL BRANDS =================
    @Override
    public List<BrandDataDto> getAllBrands(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("brandDataId").descending());
        Page<BrandData> brandPage = brandDataRepository.findAll(pageable);

        if (brandPage.isEmpty()) {
            throw new BrandNotFoundException("Brand not found");
        }

        return brandPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= EDIT BRAND =================
    @Override
    public String editBrand(Integer id, BrandDataDto dto) {

        BrandData brandData = brandDataRepository.findById(id)
                .orElseThrow(() ->
                        new BrandNotFoundException("Brand with ID " + id + " not found")
                );

        if (dto.getBrand() != null) brandData.setBrand(dto.getBrand());
        if (dto.getVariant() != null) brandData.setVariant(dto.getVariant());
        if (dto.getSubVariant() != null) brandData.setSubVariant(dto.getSubVariant());

        brandDataRepository.save(brandData);
        return "Brand updated successfully";
    }

    // ================= DELETE BRAND =================
    @Override
    public String deleteBrand(Integer id) {

        BrandData brandData = brandDataRepository.findById(id)
                .orElseThrow(() ->
                        new BrandNotFoundException("Brand with ID " + id + " not found")
                );

        brandDataRepository.delete(brandData);
        return "Brand deleted successfully";
    }

    // ================= ONLY BRANDS =================
    @Override
    public List<OnlyBrandDto> onlyBrands() {

        List<String> brands = brandDataRepository.findDistinctBrands();

        return brands.stream()
                .map(OnlyBrandDto::new)
                .collect(Collectors.toList());
    }

    // ================= VARIANTS =================
    @Override
    public List<BrandDataDto> variants(String brand) {

        List<BrandData> variants = brandDataRepository.findByBrand(brand);

        if (variants.isEmpty()) {
            throw new BrandNotFoundException(
                    "No variants found for brand " + brand
            );
        }

        return variants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= SUB VARIANTS =================
    @Override
    public List<BrandDataDto> subVariant(String brand, String variant) {

        List<BrandData> subVariants = brandDataRepository.findByBrandAndVariant(brand, variant);

        if (subVariants.isEmpty()) {
            throw new BrandNotFoundException(
                    "No sub-variants found for brand " + brand + " and variant " + variant
            );
        }

        return subVariants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ================= DTO CONVERTER =================
    private BrandDataDto convertToDto(BrandData brandData) {
        BrandDataDto dto = new BrandDataDto();
        dto.setBrandDataId(brandData.getBrandDataId());
        dto.setBrand(brandData.getBrand());
        dto.setVariant(brandData.getVariant());
        dto.setSubVariant(brandData.getSubVariant());
        return dto;
    }
}
