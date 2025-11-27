package com.spring.jwt.exception.car;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(Long message) {
        super(String.valueOf(message));
    }
}
