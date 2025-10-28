package com.spring.jwt.exception.laptop;

public class BlankFieldsException extends RuntimeException{

    public BlankFieldsException(String message){
        super(message);
    }
}
