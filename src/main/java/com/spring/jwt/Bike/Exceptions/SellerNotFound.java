package com.spring.jwt.Bike.Exceptions;

public class SellerNotFound extends RuntimeException
{
    public SellerNotFound(String message) {
        super(message);
    }
}
