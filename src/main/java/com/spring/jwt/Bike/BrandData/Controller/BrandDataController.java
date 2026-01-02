package com.spring.jwt.Bike.BrandData.Controller;

import com.spring.jwt.Bike.BrandData.Dto.BrandDataDto;
import com.spring.jwt.Bike.BrandData.Dto.ResponceDto;
import com.spring.jwt.Bike.BrandData.Dto.onlyBrandDto;
import com.spring.jwt.Bike.BrandData.Entity.BikeBrandData;
import com.spring.jwt.Bike.BrandData.Service.BrandDataService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.Bike.BrandNotFoundException;
import com.spring.jwt.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bikes/brands")
public class BrandDataController {
    @Autowired
    private BrandDataService brandDataService;

    @PostMapping("/add")
    public ResponseEntity<ResponceDto<BrandDataDto>> addBrand(
            @RequestBody BrandDataDto brandDataDto)
            throws SQLIntegrityConstraintViolationException {

        BrandDataDto saved = brandDataService.addBrand(brandDataDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponceDto<>(
                        "success",
                        "Brand data added successfully",
                        saved

                ));
    }
    @GetMapping("/all")
    public ResponseEntity<ResponceDto<List<BrandDataDto>>> getAllBrands(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "50") int pageSize
    ) {
        // Call service to get all brands
        List<BrandDataDto> data = brandDataService.getAllBrands(pageNo, pageSize);

        // Prepare response
        ResponceDto<List<BrandDataDto>> response = new ResponceDto<>();
        response.setStatus("success");
        response.setMessage("All brands fetched successfully");
        response.setData(data);  //

        return ResponseEntity.ok(response);
    }
    @GetMapping("/models")
    public ResponseEntity<ResponceDto<List<String>>> getModels(@RequestParam String brand) {
        List<String> models = brandDataService.getModelsByBrand(brand);
        return ResponseEntity.ok(
                new ResponceDto<>("success", "Models fetched successfully", models)
        );
    }
    @GetMapping("/variants")
    public ResponseEntity<ResponceDto<List<String>>> getVariants(
            @RequestParam String brand,
            @RequestParam String model) {

        List<String> variants = brandDataService.getVariantsByBrandAndModel(brand, model);
        return ResponseEntity.ok(
                new ResponceDto<>("success", "Variants fetched successfully", variants)
        );
    }
    @GetMapping("/brands/Only-brands")
    public ResponseEntity<ResponceDto<List<onlyBrandDto>>> getOnlyBrands() {
        List<onlyBrandDto> brands = brandDataService.onlyBrands();
        return ResponseEntity.ok(new ResponceDto<>("success", "Brands fetched successfully", brands));
    }

    @GetMapping("/engine-cc")
    public ResponseEntity<ResponceDto<Integer>> getEngineCc(
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam String variant
    ) {
        Integer engineCc = brandDataService.getEngineCc(brand, model, variant);

        return ResponseEntity.ok(
                new ResponceDto<>(
                        "success",
                        "Engine CC fetched successfully",
                        engineCc
                )
        );
    }




}
