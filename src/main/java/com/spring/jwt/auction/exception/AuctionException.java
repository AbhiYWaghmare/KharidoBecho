package com.spring.jwt.auction.exception;

public class AuctionException extends RuntimeException {
    public AuctionException(String msg) { super(msg); }
    public AuctionException(String msg, Throwable t) { super(msg, t); }
}
