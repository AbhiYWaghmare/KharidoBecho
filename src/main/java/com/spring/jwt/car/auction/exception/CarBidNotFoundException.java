package com.spring.jwt.car.auction.exception;

public class CarBidNotFoundException extends RuntimeException {
    public CarBidNotFoundException(Long id) { super("CarBid not found with id: " + id); }
}
