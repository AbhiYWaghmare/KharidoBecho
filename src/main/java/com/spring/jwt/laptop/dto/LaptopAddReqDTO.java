package com.spring.jwt.laptop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaptopAddReqDTO {
    private String code;
    private String message;
    private Long mobileId;
}
