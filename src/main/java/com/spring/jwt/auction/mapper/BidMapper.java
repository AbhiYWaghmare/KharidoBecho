package com.spring.jwt.auction.mapper;

import com.spring.jwt.auction.dto.BidDTO;
import com.spring.jwt.auction.entity.Bid;

public class BidMapper {

    public static BidDTO toDTO(Bid b) {
        if (b == null) return null;

        return new BidDTO(
                b.getBidId(),
                b.getAuction() != null ? b.getAuction().getAuctionId() : null,
                b.getBidderUserId(),
                b.getAmount(),
                b.getStatus() != null ? b.getStatus().name() : null,
                b.getCreatedAt()
        );
    }
}
