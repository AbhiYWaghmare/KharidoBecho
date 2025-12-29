package com.spring.jwt.car.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class  CarCreateResponseDTO {


    private String code ;
    private String message;
    private Long carId;
}
