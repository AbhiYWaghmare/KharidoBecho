package com.spring.jwt.exception.mobile;

public class MobileNotFoundException extends RuntimeException {

    // Constructor for simple message
    public MobileNotFoundException(String message) {
        super(message);
    }

    // Constructor for using only ID
    public MobileNotFoundException(Long id) {
        super("Mobile not found with id: " + id);
    }
}
