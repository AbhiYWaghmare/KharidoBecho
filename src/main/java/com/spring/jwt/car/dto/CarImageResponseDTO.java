package com.spring.jwt.car.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CarImageResponseDTO {
    private String code;
    private String message;
    private List<String> images;
}
