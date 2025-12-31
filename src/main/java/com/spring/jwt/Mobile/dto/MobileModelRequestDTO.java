package com.spring.jwt.Mobile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MobileModelRequestDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "brandId is required")
    private Long brandId;

}
