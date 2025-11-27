package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CarAuctionDTO(
        Long auctionId,
        Long carId,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal minIncrementInRupees,
        String status,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        Long highestBidderUserId
) {}
