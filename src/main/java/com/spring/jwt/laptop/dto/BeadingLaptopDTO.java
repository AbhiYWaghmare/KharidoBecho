package com.spring.jwt.laptop.dto;

import com.spring.jwt.laptop.model.AuctionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class BeadingLaptopDTO {
    private Long laptopId;
    private Double basePrice;
    private Integer minIncrement;
    private LocalDateTime closingTime;
    private Long sellerId;
    private String uniqueBeadingLaptopId;
    private AuctionStatus auctionStatus;
}
