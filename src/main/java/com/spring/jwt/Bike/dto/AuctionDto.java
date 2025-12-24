package com.spring.jwt.Bike.dto;

import lombok.Data;

@Data
public class AuctionDto {
    private Integer biddingId;
    private Long bidderId;      // user ID
    private Double amount;      // bid value
    private boolean accepted;   // is this the new highest?
    private Double highestBid;
    private Integer highestBidderId;
}
