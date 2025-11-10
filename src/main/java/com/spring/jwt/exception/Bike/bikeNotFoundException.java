package com.spring.jwt.exception.Bike;

public class bikeNotFoundException extends RuntimeException {
    public bikeNotFoundException(String message) {
        super(message);
    }
}
