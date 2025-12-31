//package com.spring.jwt.laptop.Dropdown.service;
//
//import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
//import com.spring.jwt.laptop.Dropdown.entity.LaptopModel;
//import com.spring.jwt.laptop.Dropdown.repository.LaptopBrandRepository;
//import com.spring.jwt.laptop.Dropdown.repository.LaptopModelRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class LaptopModelService {
//    private final LaptopBrandRepository brandRepository;
//    private final LaptopModelRepository modelRepository;
//
//    // ===============================
//    // 🔹 GET OR CREATE MODEL
//    // ===============================
//    public LaptopModel getOrCreateModel(String modelName, LaptopBrand brand) {
//
//        if (modelName == null || modelName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Model name cannot be empty");
//        }
//
//        return modelRepository
//                .findByBrandAndModelNameIgnoreCase(brand, modelName.trim())
//                .orElseGet(() -> {
//                    LaptopModel model = LaptopModel.builder()
//                            .modelName(modelName.trim())
//                            .brand(brand)
//                            .build();
//                    return modelRepository.save(model);
//                });
//    }
//
//    public List<LaptopModel> getModelsByBrandId(Long brandId) {
//        return modelRepository.findByBrand_BrandId(brandId);
//    }
//
//}
