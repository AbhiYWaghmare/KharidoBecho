package com.spring.jwt.car.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CarAuctionRequestDTO(
        Long mobileId,
        BigDecimal startPrice,
        BigDecimal minIncrementInRupees,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
