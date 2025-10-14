package com.spring.jwt.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO1 {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
