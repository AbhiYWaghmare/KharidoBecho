package com.spring.jwt.Bike.Exceptions;
import jakarta.validation.ConstraintViolationException;
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
    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBikeStatusNotFound(
            StatusNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_STATUS_NOT_FOUND");
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
    @ExceptionHandler(BikeImageNotFound.class)
    public ResponseEntity<Map<String, Object>> handleBikeImageNotFound(
            BikeImageNotFound ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_IMAGE_NOT_FOUND");
        errorResponse.put("errorMessage", "Bike Image Issue");
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_OPERATION_FAILED");
        errorResponse.put("errorMessage", "Check_Your_Entered_Data");
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> ResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = Map.of(
                "apiPath", request.getRequestURI(),
                "errorCode", "RESOURCE_NOT_FOUND",
                "errorMessage", ex.getMessage(),
                "errorTime", LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidBikeData.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBikeData(
            InvalidBikeData ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", request.getRequestURI());
        errorResponse.put("errorCode", "INVALID_BIKE_DATA");
        errorResponse.put("errorMessage", "Check your Entered Data");
        errorResponse.put("errorTime", java.time.LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException ex) {

        // Extract only the first validation message
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Invalid input");

        Map<String, String> response = Map.of(
                "status", "BAD_REQUEST",
                "message", message
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SellerNotFound.class)
    public ResponseEntity<Map<String, Object>> handleSellerNotFound(
            SellerNotFound ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", request.getRequestURI());
        errorResponse.put("errorCode", "INVALID_BIKE_DATA");
        errorResponse.put("errorMessage", "Seller Not Found ");
        errorResponse.put("errorTime", java.time.LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }




}
