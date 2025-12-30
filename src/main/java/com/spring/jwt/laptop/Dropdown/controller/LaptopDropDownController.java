package com.spring.jwt.laptop.Dropdown.controller;

import com.spring.jwt.laptop.Dropdown.dto.LaptopMasterResponseDTO;
import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
import com.spring.jwt.laptop.Dropdown.entity.LaptopModel;
import com.spring.jwt.laptop.Dropdown.model.*;
import com.spring.jwt.laptop.Dropdown.service.LaptopBrandService;
import com.spring.jwt.laptop.Dropdown.service.LaptopModelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/dropDown")
public class LaptopDropDownController {
    private final LaptopBrandService brandService;
    private final LaptopModelService modelService;

    // =====================================================
    // 🔹 BRAND (GET OR CREATE)
    // =====================================================
    // Frontend can send:
    // - existing brand → returned
    // - new brand → auto-created & returned
    // =====================================================

    @PostMapping("/brand")
    public String getOrCreateBrand(
            @RequestParam String brand
    ) {
        LaptopBrand brandEntity = brandService.getOrCreateBrand(brand);
        return brandEntity.getBrandName();
    }

    @GetMapping("/models/by-brand/{brandId}")
    public List<Map<String, Object>> getModelsByBrand(
            @PathVariable Long brandId
    ) {
        return modelService.getModelsByBrandId(brandId)
                .stream()
                .map(model -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", model.getModelId());
                    map.put("name", model.getModelName());
                    return map;
                })
                .toList();
    }



    // =====================================================
    // 🔹 MODEL (GET OR CREATE BY BRAND)
    // =====================================================
    // Brand will also be auto-created if new
    // =====================================================

    @PostMapping("/model")
    public String getOrCreateModel(
            @RequestParam String brand,
            @RequestParam String model
    ) {
        LaptopBrand brandEntity = brandService.getOrCreateBrand(brand);
        LaptopModel modelEntity =
                modelService.getOrCreateModel(model, brandEntity);

        return modelEntity.getModelName();
    }

    // =====================================================
    // 🔹 ENUM DROPDOWNS (STATIC – FROM CODE)
    // =====================================================

    @GetMapping("/ram")
    public List<Map<String, String>> ramOptions() {
        return Arrays.stream(RamOption.values())
                .map(r -> Map.of(
                        "value", r.getDbValue(),   // 16GB
                        "label", r.getLabel()      // 16 GB
                ))
                .toList();
    }

    @GetMapping("/storage")
    public List<Map<String, String>> storageOptions() {
        return Arrays.stream(StorageOption.values())
                .map(s -> Map.of(
                        "value", s.getDbValue(),
                        "label", s.getLabel()
                ))
                .toList();
    }

    @GetMapping("/screen-sizes")
    public List<Map<String, String>> screenSizes() {
        return Arrays.stream(ScreenSize.values())
                .map(s -> Map.of(
                        "value", s.getDbValue(),
                        "label", s.getLabel()
                ))
                .toList();
    }

    @GetMapping("/memory-types")
    public List<Map<String, String>> memoryTypes() {
        return Arrays.stream(MemoryType.values())
                .map(m -> Map.of(
                        "value", m.getDbValue(),
                        "label", m.getLabel()
                ))
                .toList();
    }

    @GetMapping("/processor-brands")
    public List<Map<String, String>> processorBrands() {
        return Arrays.stream(ProcessorBrand.values())
                .map(p -> Map.of(
                        "value", p.getDbValue(),
                        "label", p.getLabel()
                ))
                .toList();
    }

    @GetMapping("/graphics-brands")
    public List<Map<String, String>> graphicsBrands() {
        return Arrays.stream(GraphicsBrand.values())
                .map(g -> Map.of(
                        "value", g.getDbValue(),
                        "label", g.getLabel()
                ))
                .toList();
    }


    @GetMapping("/warranty")
    public List<Map<String, Object>> warrantyOptions() {
        return Arrays.stream(Warranty.values())
                .map(w -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("years", Integer.parseInt(w.getDbValue()));
                    map.put("label", w.getLabel());
                    return map;
                })
                .toList();
    }

}
