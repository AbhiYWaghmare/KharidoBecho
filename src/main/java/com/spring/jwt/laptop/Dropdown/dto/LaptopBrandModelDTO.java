package com.spring.jwt.laptop.Dropdown.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaptopBrandModelDTO {
    private Long id;
    private String brand;
    private String model;
}
