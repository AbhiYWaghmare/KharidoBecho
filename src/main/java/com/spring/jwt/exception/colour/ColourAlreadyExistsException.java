package com.spring.jwt.exception.colour;

public class ColourAlreadyExistsException extends RuntimeException{
    public ColourAlreadyExistsException(String message){
        super(message);
    }
}
