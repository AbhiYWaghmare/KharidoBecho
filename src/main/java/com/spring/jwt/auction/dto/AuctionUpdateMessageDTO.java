package com.spring.jwt.auction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record AuctionUpdateMessageDTO(
        String type, // e.g. BID_PLACED, AUCTION_STARTED, AUCTION_ENDED, WINNER_CHANGED, WINNER_ACCEPTED, UNSOLD
        Long auctionId,
        BigDecimal currentPrice,
        Long highestBidderUserId,
        List<TopBidDTO> topBids,
        OffsetDateTime offerExpiresAt
)
{
    public record TopBidDTO(
            int rank,
            Long userId,
            BigDecimal amount
    ) {}
}
