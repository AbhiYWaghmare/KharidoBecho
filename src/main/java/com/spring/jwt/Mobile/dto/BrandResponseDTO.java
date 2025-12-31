package com.spring.jwt.Mobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrandResponseDTO {
    private Long brandId;
    private String name;
    private String message;
}
