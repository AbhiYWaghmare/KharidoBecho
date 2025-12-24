package com.spring.jwt.exception.car;

public class SellerNotFoundException extends RuntimeException {

    public SellerNotFoundException(String message) {
        super(message);
    }

    public SellerNotFoundException(Long sellerId) {
        super("Seller not found with id " + sellerId);
    }
}

