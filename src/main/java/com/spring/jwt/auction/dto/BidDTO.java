package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record BidDTO(
        Long bidId,
        Long auctionId,
        Long bidderUserId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {}
