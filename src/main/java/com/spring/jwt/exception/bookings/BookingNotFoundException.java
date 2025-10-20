package com.spring.jwt.exception.bookings;

public class BookingNotFoundException extends  RuntimeException{
    public  BookingNotFoundException(String message){
        super(message);
    }
}
