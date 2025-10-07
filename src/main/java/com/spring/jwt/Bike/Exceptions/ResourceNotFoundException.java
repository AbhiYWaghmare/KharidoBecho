package com.spring.jwt.Bike.Exceptions;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
