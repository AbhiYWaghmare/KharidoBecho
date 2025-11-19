package com.spring.jwt.exception.mobile;

public class MobileRequestException extends RuntimeException {
    public MobileRequestException(String message) { super(message); }
    public MobileRequestException(String message, Throwable cause) { super(message, cause); }
}
