package com.spring.jwt.car.carbranddata.dto;

import lombok.Data;

@Data
public class CarBrandDataDto {

    private Integer brandDataId;
    private String brand;
    private String variant;
    private String subVariant;

}
