package com.spring.jwt.exception;

public class LaptopAlreadyExistsException extends RuntimeException{
    public LaptopAlreadyExistsException(String message) {
        super(message);
    }
}
