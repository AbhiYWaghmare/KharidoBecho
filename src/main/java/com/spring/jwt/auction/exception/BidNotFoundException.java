package com.spring.jwt.auction.exception;

public class BidNotFoundException extends RuntimeException {

    public BidNotFoundException(Long id) {
        super("Bid not found with id: " + id);
    }

    public BidNotFoundException(String message) {
        super(message);
    }
}
