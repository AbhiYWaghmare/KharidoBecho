package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LaptopResponseDTO {
    private String status;
    private String message;
//    private LocalDateTime localDateTime;
}
