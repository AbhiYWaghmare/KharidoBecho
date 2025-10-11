package com.spring.jwt.exception.mobile;

public class BuyerNotFoundException extends RuntimeException {
    public BuyerNotFoundException(Long buyerId) {
        super("Buyer not found with user id: " + buyerId);
    }
}
