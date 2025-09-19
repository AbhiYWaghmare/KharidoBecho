package com.spring.jwt.laptop.exceptions;

public class LaptopNotFoundException extends RuntimeException{
    public LaptopNotFoundException(String message){
        super(message);
    }
}
