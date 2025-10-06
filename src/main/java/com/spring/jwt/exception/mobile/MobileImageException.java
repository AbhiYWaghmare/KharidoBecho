package com.spring.jwt.exception.mobile;

public class MobileImageException extends RuntimeException {
    public MobileImageException(String message) {
        super(message);
    }

    public MobileImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
