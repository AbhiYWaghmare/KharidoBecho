package com.spring.jwt.auction.exception;

public class MobileNotFoundForAuctionException extends RuntimeException {
    public MobileNotFoundForAuctionException(Long mobileId) {
        super("Cannot create auction. Mobile not found for id: " + mobileId);
    }
}