package com.spring.jwt.exception.laptop;

public class LaptopAuctionNotFoundException extends RuntimeException{

    public  LaptopAuctionNotFoundException(Long id) {
        super("Auction not found with id: " + id);
    }

    public LaptopAuctionNotFoundException(String message){
        super(message);
    }
}
