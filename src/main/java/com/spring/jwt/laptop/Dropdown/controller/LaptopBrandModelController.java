package com.spring.jwt.laptop.Dropdown.controller;

import com.spring.jwt.laptop.Dropdown.dto.LaptopBrandModelDTO;
import com.spring.jwt.laptop.Dropdown.dto.ListResponseDTO;
import com.spring.jwt.laptop.Dropdown.model.*;
import com.spring.jwt.laptop.Dropdown.service.LaptopBrandModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/dropDown")
public class LaptopBrandModelController {
    private final LaptopBrandModelService service;

    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LaptopBrandModelDTO dto) {
        service.add(dto);
        return ResponseEntity.ok("Added successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "50") int pageSize) {

        return ResponseEntity.ok(
                new ListResponseDTO(
                        "Fetched successfully",
                        service.getAll(pageNo, pageSize),
                        null
                )
        );
    }

    // 🔽 DROPDOWN 1
    @GetMapping("/only-brands")
    public ResponseEntity<?> onlyBrands() {
        return ResponseEntity.ok(
                new ListResponseDTO(
                        "Brands fetched",
                        service.getOnlyBrands(),
                        null
                )
        );
    }

    // 🔽 DROPDOWN 2
    @GetMapping("/models")
    public ResponseEntity<?> models(@RequestParam String brand) {
        List<String> models = service.getModelsByBrand(brand)
                .stream()
                .map(LaptopBrandModelDTO::getModel)
                .distinct()
                .toList();

        return ResponseEntity.ok(
                new ListResponseDTO(
                        "Models fetched",
                        models,
                        null
                )
        );
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> edit(
            @RequestParam Long id,
            @RequestBody LaptopBrandModelDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        return ResponseEntity.ok(service.delete(id));
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
