package com.spring.jwt.Bike.BrandData.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponceDto<T> {

    private String status;   // success / error
    private String message;  // readable message
    private T data;          // actual response
}
