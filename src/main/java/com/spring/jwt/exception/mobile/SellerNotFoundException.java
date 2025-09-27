package com.spring.jwt.exception.mobile;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(Long sellerId) {
        super("Seller not found with id: " + sellerId);
    }
}
