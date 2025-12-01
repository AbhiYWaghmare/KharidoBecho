package com.spring.jwt.laptop.laptopAuction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record LaptopAuctionDTO(
        Long auctionId,
        Long laptopId,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal minIncrementInRupees,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long highestBidderUserId
) {}
