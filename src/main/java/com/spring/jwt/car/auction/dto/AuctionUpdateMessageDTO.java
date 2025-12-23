package com.spring.jwt.car.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AuctionUpdateMessageDTO(
        String type,
        Long auctionId,
        BigDecimal currentPrice,
        Long highestBidderUserId,
        List<TopBidDTO> topBids,
        LocalDateTime offerExpiresAt
) {

    public record TopBidDTO(
            int rank,
            Long userId,
            BigDecimal amount
    ) {}
}
