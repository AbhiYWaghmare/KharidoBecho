package com.spring.jwt.auction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class AuctionResponseDTO {
    private Long auctionId;
    private Long listingId;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
}
