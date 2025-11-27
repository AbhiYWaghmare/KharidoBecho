package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CarBidDTO(
        Long bidId,
        Long auctionId,
        Long bidderUserId,
        BigDecimal amount,
        String status,
        OffsetDateTime createdAt
) {}
