package com.spring.jwt.Mobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelResponseDTO {
    private Long modelId;
    private String modelName;
    private String brandName;
    private String message;
}
