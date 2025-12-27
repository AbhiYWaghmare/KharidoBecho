package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record AuctionDTO(
        Long auctionId,
        Long mobileId,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal minIncrementInRupees,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long highestBidderUserId
) {}