package com.spring.jwt.Bike.Exceptions;

public class BikeImageNotFound extends RuntimeException {
    public BikeImageNotFound(String message) {
        super(message);
    }
}
