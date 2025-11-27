package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CarAuctionUpdateMessageDTO(
        String type,
        Long auctionId,
        BigDecimal currentPrice,
        Long highestBidderUserId,
        List<TopBidDTO> topBids,
        OffsetDateTime offerExpiresAt
) {
    public record TopBidDTO(int rank, Long userId, BigDecimal amount) {}
}
