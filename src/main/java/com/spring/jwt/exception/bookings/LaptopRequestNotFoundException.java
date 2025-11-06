package com.spring.jwt.exception.bookings;

public class LaptopRequestNotFoundException extends  RuntimeException{
    public  LaptopRequestNotFoundException(Long bookingId){
        super("Laptop booking request not found with ID: " + bookingId);
    }
}
