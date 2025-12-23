
package com.spring.jwt.car.auction.exception;

public class InvalidAuctionStateException extends RuntimeException {
    public InvalidAuctionStateException(String message) {
        super(message);
    }

//    public InvalidAuctionStateException(Long auctionId, String currentState, String requiredState) {
//        super("Auction " + auctionId + " is in state " + currentState +
//                ", but required state is " + requiredState);
//    }
}
