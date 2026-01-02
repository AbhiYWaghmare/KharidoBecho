package com.spring.jwt.Bike.BrandData.Service;

import com.spring.jwt.Bike.BrandData.Dto.BrandDataDto;
import com.spring.jwt.Bike.BrandData.Dto.onlyBrandDto;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface BrandDataService {
       BrandDataDto addBrand(BrandDataDto brandDataDto)
            throws SQLIntegrityConstraintViolationException;

    List<BrandDataDto> getAllBrands(int pageNo, int pageSize);
    List<String> getModelsByBrand(String brand);
    List<String> getVariantsByBrandAndModel(String brand, String model);
    public List<onlyBrandDto> onlyBrands();
    //BrandDataDto updateBrand(Long brandDataId, BrandDataDto brandDataDto);

   // void deleteBrand(Long brandDataId);
   Integer getEngineCc(String brand, String model, String variant);

}
