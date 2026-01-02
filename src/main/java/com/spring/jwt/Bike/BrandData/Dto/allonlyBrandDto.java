package com.spring.jwt.Bike.BrandData.Dto;

import lombok.Data;

import java.util.List;
@Data
public class allonlyBrandDto {
    private String message;
    private List<allonlyBrandDto> list;
    private String exception;

    public allonlyBrandDto(String message) {
        this.message = message;
    }

    public allonlyBrandDto(String message, List<allonlyBrandDto> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }

}
