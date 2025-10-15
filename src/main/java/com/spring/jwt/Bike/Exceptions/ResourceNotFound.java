package com.spring.jwt.Bike.Exceptions;

public class ResourceNotFound extends RuntimeException
{
    public ResourceNotFound(String message)
    {
        super(message);
    }
}
