package com.spring.jwt.exception.laptop;

public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
    }
}
