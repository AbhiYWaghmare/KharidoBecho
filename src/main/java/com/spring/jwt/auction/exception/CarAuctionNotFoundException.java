package com.spring.jwt.auction.exception;

public class CarAuctionNotFoundException extends RuntimeException {
    public CarAuctionNotFoundException(Long id) { super("CarAuction not found with id: " + id); }
}
