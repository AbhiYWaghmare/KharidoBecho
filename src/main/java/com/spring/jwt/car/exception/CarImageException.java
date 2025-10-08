package com.spring.jwt.car.exception;

public class CarImageException extends RuntimeException{

    public CarImageException(String message){
        super(message);
    }

    public CarImageException(String message, Throwable cause){
        super(message, cause);
    }
}
