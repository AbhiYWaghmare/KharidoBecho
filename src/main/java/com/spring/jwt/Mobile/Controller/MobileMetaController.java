package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.Repository.MobileBrandRepository;
import com.spring.jwt.Mobile.Repository.MobileModelRepository;
import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.entity.MobileBrand;
import com.spring.jwt.Mobile.entity.MobileModel;
import com.spring.jwt.exception.mobile.BrandNotFoundException;
import com.spring.jwt.exception.mobile.MobileValidationException;
import com.spring.jwt.exception.mobile.ModelAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mobile-meta")
@RequiredArgsConstructor
public class MobileMetaController {

    private final MobileBrandRepository brandRepository;
    private final MobileModelRepository modelRepository;

    // ---------------- BRAND ----------------

    @PostMapping("/brands")
    public ResponseEntity<BrandResponseDTO> addBrand(@RequestParam String name) {

        if (brandRepository.existsByNameIgnoreCase(name)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BrandResponseDTO(null, null, "Brand already exists"));
        }

        MobileBrand brand = MobileBrand.builder()
                .name(name.trim().toUpperCase())
                .build();

        brandRepository.save(brand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BrandResponseDTO(
                        brand.getBrandId(),
                        brand.getName(),
                        "Brand added successfully"
                ));
    }


    @GetMapping("/brands")
    public ResponseEntity<List<MobileBrandDTO>> getBrands() {

        List<MobileBrandDTO> brands = brandRepository
                .findByDeletedFalseOrderByNameAsc()
                .stream()
                .map(b -> {
                    MobileBrandDTO dto = new MobileBrandDTO();
                    dto.setBrandId(b.getBrandId());
                    dto.setName(b.getName());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(brands);
    }


    // ---------------- MODEL ----------------

    @PostMapping("/models")
    public ResponseEntity<ModelResponseDTO> addModel(
            @Valid @RequestBody MobileModelRequestDTO request) {

        // Fetch the brand
        MobileBrand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new MobileValidationException("Brand not found"));

        // Check if a model with the same name already exists for this brand and is not deleted
        if (modelRepository.existsByNameIgnoreCaseAndBrand_BrandId(request.getName(), request.getBrandId())) {
            throw new MobileValidationException("Model already exists for this brand");
        }

        // Create and save the new model
        MobileModel model = MobileModel.builder()
                .name(request.getName().toUpperCase())
                .brand(brand)
                .deleted(false)
                .build();

        modelRepository.save(model);

        // Prepare the response DTO
        ModelResponseDTO response = new ModelResponseDTO(
                model.getModelId(),
                model.getName(),
                brand.getName(),
                "Model added successfully"
        );

        // Return 201 Created
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }




    @GetMapping("/models")
    public ResponseEntity<List<MobileModelDTO>> getModelsByBrand(
            @RequestParam Long brandId) {

        // validate brand exists
        if (!brandRepository.existsByBrandIdAndDeletedFalse(brandId)) {
            throw new BrandNotFoundException(brandId);
        }

        List<MobileModelDTO> models = modelRepository
                .findByBrand_BrandIdAndDeletedFalseOrderByNameAsc(brandId)
                .stream()
                .map(m -> {
                    MobileModelDTO dto = new MobileModelDTO();
                    dto.setModelId(m.getModelId());
                    dto.setName(m.getName());
                    dto.setBrandId(m.getBrand().getBrandId());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(models);
    }

}
