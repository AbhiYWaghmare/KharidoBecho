package com.spring.jwt.laptop.dto;

import com.spring.jwt.laptop.entity.BeadingLaptop;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BeadingLaptopWithInsDTO {
    private Long beadingLaptopId;
    private String uniqueAuctionId;

    private Laptop laptop; // send full laptop object
    private Double basePrice;
    private Double highestBid;
    private Long highestBidderId;
    private Integer minIncrement;
    private AuctionStatus auctionStatus;

    public BeadingLaptopWithInsDTO(BeadingLaptop auction, Laptop laptop) {
        this.beadingLaptopId = auction.getBeadingLaptopId();
        this.uniqueAuctionId = auction.getUniqueBeadingLaptopId();
        this.laptop = laptop;
        this.basePrice = auction.getBasePrice();
        this.highestBid = auction.getHighestBid();
        this.highestBidderId = auction.getHighestBidderId();
        this.minIncrement = auction.getMinIncrement();
        this.auctionStatus = auction.getAuctionStatus();
    }
}
