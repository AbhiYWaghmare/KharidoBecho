package com.spring.jwt.exception.car;

public class CarImageException extends RuntimeException {
    public CarImageException(String message) {
        super(message);
    }

    public CarImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
