package com.spring.jwt.car.carbranddata.dto;

import lombok.Data;
import java.util.List;

@Data
public class CarAllBrandDataDto {

    private String message;
    private List<?> list;
    private String exception;

    public CarAllBrandDataDto(String message, List<?> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
