package com.spring.jwt.exception.Bike;

public class StatusNotFoundException extends RuntimeException {

    public StatusNotFoundException(String message) {
        super(message);
    }
}
