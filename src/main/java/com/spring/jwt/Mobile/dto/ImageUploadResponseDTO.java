package com.spring.jwt.Mobile.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ImageUploadResponseDTO {
    private String code;
    private String message;
    private List<String> images;
}
