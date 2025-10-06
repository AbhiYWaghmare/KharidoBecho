package com.spring.jwt.car.exception;

// For missing/null values
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}



