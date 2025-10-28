package com.spring.jwt.exception.car;

public class CarNotFoundException extends RuntimeException  {

    // Constructor for simple message
    public CarNotFoundException(String message) {
        super(message);
    }

    // Constructor for using only ID
    public CarNotFoundException(Long id) {
        super("Car not found with id: " + id);
    }
}
