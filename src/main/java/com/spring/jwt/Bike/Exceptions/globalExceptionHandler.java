
package com.spring.jwt.Bike.Exceptions;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
        String message = ex.getLocalizedMessage();

        // ===== Handle Duplicate Registration Number =====
        if (message != null && message.contains("registration_number")) {
            message = "Registration number already exists. Please provide a unique registration number.";
        }
        // ===== Handle Other Runtime Exceptions =====
        else {
            message = "An unexpected error occurred: " + message;
        }

        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_OPERATION_FAILED");
        errorResponse.put("errorMessage", message);
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }







    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Map<String, Object>> ResourceNotFoundException(
            ResourceNotFound ex,
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
        errorResponse.put("errorMessage", ex.getLocalizedMessage());
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



////    @ExceptionHandler(UnrecognizedPropertyException.class)
////    public ResponseEntity<Map<String, Object>> handleUnknownField(UnrecognizedPropertyException ex) {
////        Map<String, Object> error = new HashMap<>();
////        error.put("errorTime", LocalDateTime.now());
////       // error.put("errorMessage", "Unknown field: " + ex.getPropertyName());
////        error.put("errorMessage", "Unknown field added"+ex.getLocalizedMessage() );
////        error.put("errorCode", "VALIDATION_FAILED");
////        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
////    }

//JSON  Exception added for fuel type And status

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<Map<String, Object>> handleJsonParseError(
        HttpMessageNotReadableException ex,
        HttpServletRequest request) {

    Map<String, Object> errorResponse = new HashMap<>();
    String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

    if (message != null) {
        if (message.contains("Unrecognized field")) {
            String fieldName = message.split("Unrecognized field \"")[1].split("\"")[0];
            message = "Invalid field '" + fieldName + "' is not allowed in request.";
        }
        if (message.contains("FuelType")) {
            message = "Invalid fuel type. Allowed values are: PETROL or EV.";
        }
        if (message.contains("bikeStatus")) {
            message = "Invalid status. Allowed values are: ACTIVE, AVAILABLE, INACTIVE, DELETED, or NEW.";
        }
        if (message.contains("Cannot deserialize value of type") && message.contains("Integer")) {
            message = "Manufacture year must be a whole number (no decimals allowed).";
        }
        if (message.contains("registration_number")) {
            message = "Registration number already exists. Please provide a unique registration number.";
        }
    } else {
        message = "Invalid request data or format.";
    }

    errorResponse.put("apiPath", request.getRequestURI());
    errorResponse.put("errorCode", "INVALID_INPUT");
    errorResponse.put("errorMessage", message);
    errorResponse.put("errorTime", LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
}


}
