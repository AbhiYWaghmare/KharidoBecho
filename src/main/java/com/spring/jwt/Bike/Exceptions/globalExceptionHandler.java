package com.spring.jwt.Bike.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(bikeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBikeNotFound(
            bikeNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_NOT_FOUND");
        errorResponse.put("errorMessage", ex.getMessage());
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "INTERNAL_SERVER_ERROR");
        errorResponse.put("errorMessage", "An unexpected error occurred");
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
