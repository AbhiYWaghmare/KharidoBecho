package com.spring.jwt.Bike.Websocket.Dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
public record BikeBidDto(
        Long bidId,
        Long auctionId,
        Long bidderUserId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {}

