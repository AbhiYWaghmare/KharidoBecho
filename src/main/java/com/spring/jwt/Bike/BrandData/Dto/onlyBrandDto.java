package com.spring.jwt.Bike.BrandData.Dto;

import lombok.Data;

@Data
public class onlyBrandDto {
    private String brand;

    public onlyBrandDto(String brand) {
        this.brand = brand;
    }
}
