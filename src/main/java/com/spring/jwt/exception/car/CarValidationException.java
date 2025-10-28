package com.spring.jwt.exception.car;

public class CarValidationException extends RuntimeException {
    public CarValidationException(String message) {
        super(message);
    }
}
