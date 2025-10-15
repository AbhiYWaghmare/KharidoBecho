package com.spring.jwt.car.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ************************************************************
 *  Global Exception Handler for Car Module
 *  Author : Abhishek Patil
 *  Description : Handles all exceptions
 * ************************************************************
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ======================================================== //
    // ✅ 1. Resource Not Found
    // Returns 404 when entity not found
    // ======================================================== //
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error(" Resource not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ======================================================== //
    // ✅ 2. Car already exists
    // Returns 409 when duplicate car registration is found
    // ======================================================== //
    @ExceptionHandler(CarAlreadyExists.class)
    public ResponseEntity<Map<String, Object>> handleCarAlreadyExists(CarAlreadyExists ex) {
        log.error(" Car already exists: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ======================================================== //
    // ✅ 3. Invalid request (custom business rules)
    // Returns 400 for logical errors (e.g., invalid data/range)
    // ======================================================== //
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(InvalidRequestException ex) {
        log.error(" Invalid request: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ======================================================== //
    // ✅ 4. Seller not found
    // Returns 404 when seller entity not present
    // ======================================================== //
    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSellerNotFound(SellerNotFoundException ex) {
        log.error(" Seller not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ======================================================== //
    // ✅ 5. Validation Errors (@Valid)
    // Aggregates all invalid fields into one structured response
    // ======================================================== //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error(" Validation failed: {}", ex.getMessage());

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "FAILED");
        response.put("message", "Validation failed for one or more fields");
        response.put("errors", fieldErrors);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ======================================================== //
    // ✅ 6. Invalid JSON or Type Mismatch
    // Handles wrong data type, bad enum, malformed payload, etc.
    // ======================================================== //
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";
        String userMessage;

        if (message.toLowerCase().contains("year")) {
            userMessage = "Invalid year format. Year must be a number between 2000 and 2025.";
        } else if (message.toLowerCase().contains("price")) {
            userMessage = "Invalid price format. Must be a positive number between 1,000 and 99,999,999.";
        } else if (message.toLowerCase().contains("boolean")) {
            userMessage = "Invalid boolean value. Use true or false for fields like abs, acFeature, airbag, etc.";
        } else if (message.toLowerCase().contains("transmission")) {
            userMessage = "Invalid transmission type. Allowed values: Manual, Automatic, CVT, DCT.";
        } else if (message.toLowerCase().contains("cartype")) {
            userMessage = "Invalid carType value. Allowed types: Sedan, Hatchback, SUV, etc.";
        } else if (message.toLowerCase().contains("color")) {
            userMessage = "Invalid color format. Use only letters and spaces.";
        } else if (message.toLowerCase().contains("localdate") || message.toLowerCase().contains("date")) {
            userMessage = "Invalid date format. Expected YYYY-MM-DD (e.g., 2024-12-20).";
        } else if (message.toLowerCase().contains("kmdriven")) {
            userMessage = "Invalid kmDriven value. Must be an integer between 0 and 2,000,000.";
        } else if (message.toLowerCase().contains("integer") || message.toLowerCase().contains("number format")) {
            userMessage = "Invalid numeric value. Please provide a valid number.";
        } else if (message.toLowerCase().contains("status")) {
            userMessage = "Invalid carStatus. Allowed values: PENDING, ACTIVE, SOLD, DELETED, DEACTIVATE, AVAILABLE.";
        } else if (message.toLowerCase().contains("registration")) {
            userMessage = "Invalid registration format. Example: 'MH 12 AB 1234'.";
        } else if (message.toLowerCase().contains("city")) {
            userMessage = "Invalid city name. Use only alphabets and spaces.";
        } else if (message.toLowerCase().contains("area")) {
            userMessage = "Invalid area name. Minimum 2 characters, letters only.";
        } else if (message.toLowerCase().contains("description")) {
            userMessage = "Invalid description length. Should be between 5 and 500 characters.";
        } else {
            userMessage = "Invalid input format. Please verify all field types and values.";
        }

        log.error("Invalid JSON format: {}", userMessage);

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "FAILED");
        error.put("message", userMessage);
        error.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ======================================================== //
    // ✅ 7. Unknown or Extra Fields
    // Handles unexpected properties in JSON request
    // ======================================================== //
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, Object>> handleUnknownProperty(UnrecognizedPropertyException ex) {
        log.error(" Unknown field detected: {}", ex.getPropertyName());
        String message = "Unexpected field '" + ex.getPropertyName() + "' detected. Please remove extra or misspelled fields.";
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    // ======================================================== //
    // ✅ 8. Fallback - Catch All
    // Handles all other uncaught exceptions gracefully
    // ======================================================== //
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error(" Unexpected error occurred: ", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    // ======================================================== //
    // ✅ Utility method for consistent error response
    // ======================================================== //
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "FAILED");
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
