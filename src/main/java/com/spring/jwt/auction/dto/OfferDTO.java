package com.spring.jwt.auction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class OfferDTO {
    private Long auctionId;
    private Long listingId;
    private Long bidderUserId;
    private BigDecimal amount;
    private OffsetDateTime offerExpiresAt;
    private Integer rank;
}
