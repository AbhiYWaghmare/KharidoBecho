package com.spring.jwt.car.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //========================================================//
    // ✅ 1. Resource Not Found
    // Returns 404 when entity not found
    //========================================================//
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //========================================================//
    // ✅ 2. Car already exists
    // Returns 409 when car with same registration exists
    //========================================================//
    @ExceptionHandler(CarAlreadyExists.class)
    public ResponseEntity<Map<String, Object>> handleCarExists(CarAlreadyExists ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    //========================================================//
    // ✅ 3. Invalid request (custom business rules)
    // Returns 400 for logical errors (e.g., invalid range, unsupported type)
    //========================================================//
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(InvalidRequestException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //========================================================//
    // ✅ 4. Seller not found
    // Returns 404 if seller entity is not present
    //========================================================//
    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSellerNotFound(SellerNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //========================================================//
    // ✅ 5. Bean Validation errors (@Valid)
    // Aggregates all missing/invalid fields into one response
    // Returns 400
    //========================================================//
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "FAILED");
        response.put("message", "Validation failed for one or more fields");
        response.put("errors", fieldErrors); // aggregated errors
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //========================================================//
    // ✅ 6. Invalid JSON, wrong types, extra fields
    // Handles string instead of number, invalid boolean, enum, extra properties, etc.
    // Returns 400 with field-specific guidance
    //========================================================//
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";

        String userMessage;

        if (message.toLowerCase().contains("year")) {
            userMessage = "Invalid year format. Year must be a number between 2000 and 2025."; // NEW
        } else if (message.toLowerCase().contains("price")) {
            userMessage = "Invalid price format. Price must be a valid number greater than 0 and within range 1000–99999999."; // NEW
        } else if (message.toLowerCase().contains("boolean")) {
            userMessage = "Invalid boolean value. Use true or false for fields like abs, acFeature, airbag, etc."; // NEW
        } else if (message.toLowerCase().contains("transmission")) {
            userMessage = "Invalid transmission value. Allowed: Manual, Automatic, CVT, DCT."; // NEW
        } else if (message.toLowerCase().contains("cartype")) {
            userMessage = "Invalid carType value. Allowed types: Sedan, Hatchback, SUV, etc."; // NEW
        } else if (message.toLowerCase().contains("color")) {
            userMessage = "Invalid color format. Color must contain only letters and spaces."; // NEW
        } else if (message.toLowerCase().contains("localdate") || message.toLowerCase().contains("date")) {
            userMessage = "Invalid date format. Expected YYYY-MM-DD or DD-MM-YYYY."; // NEW
        } else if (message.toLowerCase().contains("kmdriven")) {
            userMessage = "Invalid kmDriven value. Must be an integer between 0 and 2,000,000."; // NEW
        } else if (message.toLowerCase().contains("long") || message.toLowerCase().contains("integer")
                || message.toLowerCase().contains("number format")) {
            userMessage = "Invalid numeric value. Please provide a valid number."; // NEW
        } else if (message.toLowerCase().contains("status")) {
            userMessage = "Invalid carStatus value. Allowed values: PENDING, ACTIVE, SOLD, DELETED, DEACTIVATE, AVAILABLE"; // NEW
        } else if (message.toLowerCase().contains("registration")) {
            userMessage = "Invalid registration format. Must be like 'MH 12 AB 1234'."; // NEW
        } else if (message.toLowerCase().contains("city")) {
            userMessage = "Invalid city name. Must contain only letters and spaces."; // NEW
        } else if (message.toLowerCase().contains("area")) {
            userMessage = "Invalid area name. Minimum 2 characters, letters only."; // NEW
        } else if (message.toLowerCase().contains("description")) {
            userMessage = "Invalid description length. Must be between 5 and 500 characters."; // NEW

        }
        else if (message.toLowerCase().contains("localdate") || message.toLowerCase().contains("date")) {
            userMessage = "Invalid date format. Expected format: YYYY-MM-DD (e.g., 2024-12-20).";
        }
        else if (message.toLowerCase().contains("boolean")) {
            userMessage = "Invalid boolean value. Please use true or false for fields like abs, acFeature, airbag, powerWindowFeature, sunroof, musicFeature, etc.";
        }


        else {
            userMessage = "Invalid input format. Please verify all field types and values."; // fallback
        }

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "FAILED");
        error.put("message", userMessage);
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //========================================================//
    // ✅ 7. Extra/Unknown fields
    // Handles unexpected fields in JSON
    //========================================================//
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, Object>> handleUnknownProperties(UnrecognizedPropertyException ex) {
        String message = "Unexpected field(s) detected. Please remove extra fields.";
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    //========================================================//
    // ✅ 8. Catch-all fallback
    // Returns 500 for unexpected exceptions
    //========================================================//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        ex.printStackTrace(); // log for debugging
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    //========================================================//
    // ✅ Utility method for consistent error response
    // Includes status, message, timestamp
    //========================================================//
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "FAILED");
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
