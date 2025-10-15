package com.spring.jwt.Bike.Exceptions;

public class InvalidBikeData extends RuntimeException {
    public InvalidBikeData(String message) {
        super(message);
    }
}
