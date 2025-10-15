package com.spring.jwt.Bike.Exceptions;

public class bikeNotFoundException extends RuntimeException {
    public bikeNotFoundException(String message) {
        super(message);
    }
}
