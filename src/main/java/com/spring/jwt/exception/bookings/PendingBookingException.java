package com.spring.jwt.exception.bookings;

public class PendingBookingException extends RuntimeException{

    public PendingBookingException(String message){
        super(message);
    }
}
