package com.spring.jwt.car.exception;

public class CarAlreadyExists extends RuntimeException {
    public CarAlreadyExists(String message) {
        super(message);
    }
}
