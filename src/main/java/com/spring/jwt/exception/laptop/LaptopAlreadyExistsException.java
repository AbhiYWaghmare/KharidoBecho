package com.spring.jwt.exception.laptop;

public class LaptopAlreadyExistsException extends RuntimeException{
    public LaptopAlreadyExistsException(String message) {
        super(message);
    }
}
