package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LaptopResponseDTO {
    private String status;
    private String message;
}
