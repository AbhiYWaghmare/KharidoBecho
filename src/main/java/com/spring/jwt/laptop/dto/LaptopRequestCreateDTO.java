package com.spring.jwt.laptop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaptopRequestCreateDTO {
    private Long laptopId;
    private Long buyerUserId;
    private String message;
}
