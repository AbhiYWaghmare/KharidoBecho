package com.spring.jwt.exception.car;

public class BuyerNotFoundException extends RuntimeException {
    public BuyerNotFoundException(String message) {
        super(message);
    }
}
