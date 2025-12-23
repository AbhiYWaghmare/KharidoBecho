package com.spring.jwt.car.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AuctionDTO(
        Long auctionId,
        Long carId,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal minIncrementInRupees,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long highestBidderUserId
) {}
