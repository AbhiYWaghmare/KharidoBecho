package com.spring.jwt.Bike.dto;

import lombok.Data;

@Data
public class BidRequest {
    private Long auctionId;
    private Long userId;
    private Double amount;
}
