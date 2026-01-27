package com.spring.jwt.auction.exception;

public class BadBidException extends RuntimeException {

    public BadBidException(String message) {
        super(message);
    }
}
