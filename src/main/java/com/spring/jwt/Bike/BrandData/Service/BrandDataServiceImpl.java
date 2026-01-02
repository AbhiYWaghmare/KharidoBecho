package com.spring.jwt.Bike.BrandData.Service;

import com.spring.jwt.Bike.BrandData.Dto.BrandDataDto;
import com.spring.jwt.Bike.BrandData.Dto.onlyBrandDto;
import com.spring.jwt.Bike.BrandData.Entity.BikeBrandData;
import com.spring.jwt.Bike.BrandData.Repository.BrandDataRepository;
import com.spring.jwt.entity.BrandData;
import com.spring.jwt.exception.Bike.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandDataServiceImpl implements BrandDataService {
    @Autowired
    private BrandDataRepository brandDataRepository;

    @Override
    public BrandDataDto addBrand(BrandDataDto brandDataDto)
            throws SQLIntegrityConstraintViolationException {

        Optional<BikeBrandData> existingBrand =
                brandDataRepository.findByBrandAndModelAndVariant(
                        brandDataDto.getBrand(),
                        brandDataDto.getModel(),
                        brandDataDto.getVariant()
                );

        if (existingBrand.isPresent()) {
            throw new SQLIntegrityConstraintViolationException(
                    "Brand with the same Brand, Model, and Variant already exists"
            );
        }

        BikeBrandData brandData = new BikeBrandData();
        brandData.setBrand(brandDataDto.getBrand());
        brandData.setModel(brandDataDto.getModel());
        brandData.setVariant(brandDataDto.getVariant());

        brandDataRepository.save(brandData);

        // return saved data (good practice)
        BrandDataDto responseDto = new BrandDataDto();
        responseDto.setBrandDataId(brandData.getBrandDataId());
        responseDto.setBrand(brandData.getBrand());
        responseDto.setModel(brandData.getModel());
        responseDto.setVariant(brandData.getVariant());

        return responseDto;
    }

    @Override
    public List<BrandDataDto> getAllBrands(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("brandDataId").descending());
        Page<BikeBrandData> brandPage = brandDataRepository.findAll(pageable);

        // convert each BikeBrandData entity to BrandDataDto
        return brandPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // helper method
    private BrandDataDto convertToDto(BikeBrandData brandData) {
        BrandDataDto dto = new BrandDataDto();
        dto.setBrandDataId(brandData.getBrandDataId());
        dto.setBrand(brandData.getBrand());
        dto.setModel(brandData.getModel());
        dto.setVariant(brandData.getVariant());
        return dto;
    }
    @Override
    public List<String> getModelsByBrand(String brand) {
        List<BikeBrandData> models = brandDataRepository.findByBrand(brand);
        if (models.isEmpty()) {
            throw new BrandNotFoundException("No models found for brand: " + brand);
        }
        // Extract only the model names
        return models.stream()
                .map(BikeBrandData::getModel)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getVariantsByBrandAndModel(String brand, String model) {
        List<BikeBrandData> variants = brandDataRepository.findByBrandAndModel(brand, model);
        if (variants.isEmpty()) {
            throw new BrandNotFoundException("No variants found for brand: " + brand + " and model: " + model);
        }
        // Extract only the variant names
        return variants.stream()
                .map(BikeBrandData::getVariant)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<onlyBrandDto> onlyBrands() {
        List<String> brands = brandDataRepository.findDistinctBrands();
        return brands.stream()
                .map(onlyBrandDto::new)
                .collect(Collectors.toList());
    }

//    @Override
//    public BrandDataDto updateBrand(Long brandDataId, BrandDataDto brandDataDto) {
//
//        BikeBrandData existingBrand = brandDataRepository.findById(brandDataId)
//                .orElseThrow(() -> new BrandNotFoundException(
//                        "Brand data not found with id: " + brandDataId));
//
//        // update fields only if provided
//        if (brandDataDto.getBrand() != null && !brandDataDto.getBrand().isBlank()) {
//            existingBrand.setBrand(brandDataDto.getBrand());
//        }
//
//        if (brandDataDto.getModel() != null && !brandDataDto.getModel().isBlank()) {
//            existingBrand.setModel(brandDataDto.getModel());
//        }
//
//        if (brandDataDto.getVariant() != null && !brandDataDto.getVariant().isBlank()) {
//            existingBrand.setVariant(brandDataDto.getVariant());
//        }
//
//        // prevent duplicate (brand + model + variant)
//        Optional<BikeBrandData> duplicate =
//                brandDataRepository.findByBrandAndModelAndVariant(
//                        existingBrand.getBrand(),
//                        existingBrand.getModel(),
//                        existingBrand.getVariant()
//                );
//
//        if (duplicate.isPresent() && !duplicate.get().getBrandDataId().equals(brandDataId)) {
//            throw new BrandNotFoundException(
//                    "Brand with same Brand, Model and Variant already exists");
//        }
//
//        BikeBrandData saved = brandDataRepository.save(existingBrand);
//        return convertToDto(saved);
//    }
//
//    @Override
//    public void deleteBrand(Long brandDataId) {
//
//        BikeBrandData brandData = brandDataRepository.findById(brandDataId)
//                .orElseThrow(() -> new BrandNotFoundException(
//                        "Brand data not found with id: " + brandDataId));
//
//        brandDataRepository.delete(brandData);
//    }

    @Override
    public Integer getEngineCc(String brand, String model, String variant) {

        Integer engineCc =
                brandDataRepository.findEngineCcByBrandModelVariant(
                        brand, model, variant
                );

        if (engineCc == null) {
            throw new BrandNotFoundException(
                    "Engine CC not found for brand: " + brand +
                            ", model: " + model +
                            ", variant: " + variant
            );
        }

        return engineCc;
    }


}
