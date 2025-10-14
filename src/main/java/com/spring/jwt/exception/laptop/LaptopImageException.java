package com.spring.jwt.exception.laptop;

public class LaptopImageException extends RuntimeException{

    public LaptopImageException(String message){
        super(message);
    }
    public LaptopImageException(String message, Throwable cause) {
        super(message, cause);
    }


}
