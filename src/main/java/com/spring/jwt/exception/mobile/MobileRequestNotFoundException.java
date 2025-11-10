package com.spring.jwt.exception.mobile;

public class MobileRequestNotFoundException extends RuntimeException {
    public MobileRequestNotFoundException(Long id) {
        super("Mobile request not found: " + id);
    }
}
