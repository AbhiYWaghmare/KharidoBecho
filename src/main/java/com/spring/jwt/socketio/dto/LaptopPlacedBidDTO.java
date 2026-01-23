package com.spring.jwt.socketio.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class LaptopPlacedBidDTO  {

    private Long auctionId;

    private Long bidderId;

    private Double bidAmount;

    private String bidTime;
}
