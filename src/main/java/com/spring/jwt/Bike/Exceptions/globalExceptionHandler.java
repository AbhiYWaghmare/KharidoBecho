package com.spring.jwt.Bike.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class globalExceptionHandler {
    // Specific handler for BikeNotFoundException
    @ExceptionHandler(bikeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBikeNotFoundException(bikeNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=/bikes");
        errorResponse.put("errorCode", "BIKE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
