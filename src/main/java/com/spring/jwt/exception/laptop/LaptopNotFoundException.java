package com.spring.jwt.exception.laptop;

public class LaptopNotFoundException extends RuntimeException{
    public LaptopNotFoundException(String message){
        super(message);
    }

    // Constructor for using only ID
    public LaptopNotFoundException(Long id) {
        super("laptop not found with id: " + id);
    }
}
