package com.spring.jwt.car.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) { super(message); }
}
