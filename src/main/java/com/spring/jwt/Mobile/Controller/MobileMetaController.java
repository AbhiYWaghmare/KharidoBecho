package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.entity.Brand;
import com.spring.jwt.Mobile.entity.MobileModel;
import com.spring.jwt.Mobile.Repository.BrandRepository;
import com.spring.jwt.Mobile.Repository.MobileModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mobile-meta")
@RequiredArgsConstructor
public class MobileMetaController {

    private final BrandRepository brandRepository;
    private final MobileModelRepository mobileModelRepository;

    // ============================
    // 🔹 BRAND AUTOCOMPLETE
    // ============================
    // GET /api/v1/mobile-meta/brands?query=sa
    @GetMapping("/brands")
    public List<String> getBrands(
            @RequestParam(defaultValue = "") String query
    ) {
        return brandRepository
                .findAll()
                .stream()
                .map(Brand::getName)
                .filter(name ->
                        name.toLowerCase().startsWith(query.toLowerCase())
                )
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }

    // ============================
    // 🔹 MODEL AUTOCOMPLETE (BY BRAND)
    // ============================
    // GET /api/v1/mobile-meta/models?brand=Samsung&query=ga
    @GetMapping("/models")
    public List<String> getModels(
            @RequestParam String brand,
            @RequestParam(defaultValue = "") String query
    ) {
        Brand brandEntity = brandRepository
                .findByNameIgnoreCase(brand)
                .orElseThrow(() ->
                        new IllegalArgumentException("Brand not found: " + brand)
                );

        return mobileModelRepository
                .findTop10ByBrandAndNameStartingWithIgnoreCase(
                        brandEntity, query
                )
                .stream()
                .map(MobileModel::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
