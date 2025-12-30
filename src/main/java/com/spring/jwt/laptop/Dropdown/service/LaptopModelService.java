package com.spring.jwt.laptop.Dropdown.service;

import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
import com.spring.jwt.laptop.Dropdown.entity.LaptopModel;
import com.spring.jwt.laptop.Dropdown.repository.LaptopBrandRepository;
import com.spring.jwt.laptop.Dropdown.repository.LaptopModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LaptopModelService {
    private final LaptopBrandRepository brandRepository;
    private final LaptopModelRepository modelRepository;

//    // ===============================
//    // 🔹 AUTOCOMPLETE MODELS BY BRAND
//    // ===============================
//    public List<String> searchModels(String brandName, String query) {
//
//        LaptopBrand brand = brandRepository
//                .findByBrandNameIgnoreCase(brandName)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("Brand not found: " + brandName)
//                );
//
//        return modelRepository
//                .findTop10ByBrandAndModelNameStartingWithIgnoreCase(
//                        brand, query
//                )
//                .stream()
//                .map(LaptopModel::getModelName)
//                .toList();
//    }

    // ===============================
    // 🔹 GET OR CREATE MODEL
    // ===============================
    public LaptopModel getOrCreateModel(String modelName, LaptopBrand brand) {

        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }

        return modelRepository
                .findByBrandAndModelNameIgnoreCase(brand, modelName.trim())
                .orElseGet(() -> {
                    LaptopModel model = LaptopModel.builder()
                            .modelName(modelName.trim())
                            .brand(brand)
                            .build();
                    return modelRepository.save(model);
                });
    }
}
