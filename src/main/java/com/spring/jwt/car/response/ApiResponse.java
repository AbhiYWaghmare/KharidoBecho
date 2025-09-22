package com.spring.jwt.car.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;   // "SUCCESS" or "FAILURE"
    private String message;
//    private T data
    private LocalDateTime timestamp;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
//        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
