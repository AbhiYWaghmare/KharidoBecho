package com.spring.jwt.exception;

public class LaptopNotFoundException extends RuntimeException{
    public LaptopNotFoundException(String message){
        super(message);
    }
}
