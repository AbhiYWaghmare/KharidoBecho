package com.spring.jwt.laptop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ImageUploadResponseDTO {
    private String code;
    private String message;
    private List<String> images;
}
