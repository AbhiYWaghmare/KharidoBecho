package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaptopResponseDTO {
    private String status;
    private String message;
    private String code;
    private Integer statusCode;
    private LocalDateTime timeStamp;
    private String exception;
    private String apiPath;
    private String imageUrl;
    private Long laptopId;

}
