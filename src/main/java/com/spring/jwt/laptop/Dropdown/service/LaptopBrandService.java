//package com.spring.jwt.laptop.Dropdown.service;
//
//import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
//import com.spring.jwt.laptop.Dropdown.repository.LaptopBrandRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class LaptopBrandService {
//    private final LaptopBrandRepository brandRepository;
//
//    // ===============================
//    // 🔹 AUTOCOMPLETE
//    // ===============================
////    public List<String> searchBrands(String query) {
////        return brandRepository
////                .findTop10ByBrandNameStartingWithIgnoreCase(query)
////                .stream()
////                .map(LaptopBrand::getBrandName)
////                .toList();
////    }
//
//
//    // ===============================
//    //  GET OR CREATE BRAND
//    // ===============================
//    public LaptopBrand getOrCreateBrand(String brandName) {
//
//        if (brandName == null || brandName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Brand name cannot be empty");
//        }
//
//        return brandRepository
//                .findByBrandNameIgnoreCase(brandName.trim())
//                .orElseGet(() -> {
//                    LaptopBrand brand = LaptopBrand.builder()
//                            .brandName(brandName.trim())
//                            .build();
//                    return brandRepository.save(brand);
//                });
//    }
//}
