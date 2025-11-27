package com.spring.jwt.laptop.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class AuctionDTO {
    private Long beadingLaptopId;
    private Long laptopId;
    private Double highestBid;
    private Long highestBidderId;
    private LocalDateTime closingTime;
    private String message;

}
