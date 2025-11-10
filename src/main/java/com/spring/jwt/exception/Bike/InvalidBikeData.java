package com.spring.jwt.exception.Bike;

public class InvalidBikeData extends RuntimeException {
    public InvalidBikeData(String message) {
        super(message);
    }
}
