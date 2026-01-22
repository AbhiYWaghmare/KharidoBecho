package com.spring.jwt.car.carbranddata.controller;

import com.spring.jwt.car.carbranddata.dto.CarBrandDataDto;
import com.spring.jwt.car.carbranddata.dto.CarOnlyBrandDto;
import com.spring.jwt.car.carbranddata.service.CarBrandDataService;
import com.spring.jwt.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/car/brands")
public class CarBrandDataController {

    @Autowired
    private CarBrandDataService carbrandDataService;

    // ================= ADD BRAND =================
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<CarBrandDataDto>> addBrand(
            @RequestBody CarBrandDataDto brandDataDto)
            throws SQLIntegrityConstraintViolationException {

        CarBrandDataDto saved = carbrandDataService.addBrand(brandDataDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(
                        "success",
                        saved,
                        "Brand data added successfully"
                ));
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<CarBrandDataDto>>> getAllBrands(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "50") Integer pageSize) {

        List<CarBrandDataDto> data =
                carbrandDataService.getAllBrands(pageNo, pageSize);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        "success",
                        data,
                        "All brands fetched successfully"
                )
        );
    }

    // ================= EDIT =================
    @PatchMapping("/edit")
    public ResponseEntity<ResponseDto<String>> editBrand(
            @RequestParam Integer id,
            @RequestBody CarBrandDataDto dto) {

        String message = carbrandDataService.editBrand(id, dto);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        "success",
                        message,
                        "Brand updated successfully"
                )
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<String>> deleteBrand(
            @RequestParam Integer id) {

        String message = carbrandDataService.deleteBrand(id);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        "success",
                        message,
                        "Brand deleted successfully"
                )
        );
    }
    // ================= ONLY BRANDS =================
    @GetMapping("/only-brands")
    public ResponseEntity<ResponseDto<List<CarOnlyBrandDto>>> onlyBrands() {
        List<CarOnlyBrandDto> brands = carbrandDataService.onlyBrands();

        return ResponseEntity.ok(
                new ResponseDto<>(
                        "success",      // status
                        brands,         // data
                        "Brands fetched successfully"  // message
                )
        );
    }



        // ================= VARIANTS =================
        @GetMapping("/variants")
        public ResponseEntity<ResponseDto<List<String>>> variants(@RequestParam String brand) {
            List<String> variants = carbrandDataService.variants(brand)
                    .stream()
                    .map(CarBrandDataDto::getVariant)
                    .distinct()
                    .toList();

            return ResponseEntity.ok(
                    new ResponseDto<>(
                            "success",
                            variants,
                            "Variants fetched successfully"
                    )
            );
        }

        // ================= SUB-VARIANTS =================
        @GetMapping("/sub-variants")
        public ResponseEntity<ResponseDto<List<String>>> subVariants(
                @RequestParam String brand,
                @RequestParam String variant) {

            List<String> subVariants = carbrandDataService.subVariant(brand, variant)
                    .stream()
                    .map(CarBrandDataDto::getSubVariant)
                    .distinct()
                    .toList();

            return ResponseEntity.ok(
                    new ResponseDto<>(
                            "success",
                            subVariants,
                            "Sub-variants fetched successfully"
                    )
            );
        }

    }



