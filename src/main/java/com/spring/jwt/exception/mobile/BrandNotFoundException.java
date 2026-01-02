package com.spring.jwt.exception.mobile;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long brandId) {
        super("Brand not found with id: " + brandId);
    }
}
