package com.spring.jwt.car.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponseDTO<T> {
    private String message;
    private T data;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }
}

