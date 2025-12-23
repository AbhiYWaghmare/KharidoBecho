package com.spring.jwt.car.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidDTO(
        Long bidId,
        Long auctionId,
        Long bidderUserId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {}
