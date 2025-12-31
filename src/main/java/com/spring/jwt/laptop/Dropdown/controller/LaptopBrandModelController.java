package com.spring.jwt.laptop.Dropdown.controller;

import com.spring.jwt.laptop.Dropdown.dto.LaptopBrandModelDTO;
import com.spring.jwt.laptop.Dropdown.dto.ListResponseDTO;
import com.spring.jwt.laptop.Dropdown.service.LaptopBrandModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(
                new ListResponseDTO(
                        "Models fetched",
                        service.getModelsByBrand(brand),
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
}
