package com.spring.jwt.Bike.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status; // "SUCCESS" or "FAILURE"
    private String message;
    private Object data;
    private Long bikeId;
    private LocalDateTime timestamp;

    // Constructor
    public ApiResponse(String status, String message,Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();


    }
    public ApiResponse(String status, String message, Long bikeId) {
        this.status = status;
        this.message = message;
        this.bikeId = bikeId;
        this.timestamp = LocalDateTime.now();
    }
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
