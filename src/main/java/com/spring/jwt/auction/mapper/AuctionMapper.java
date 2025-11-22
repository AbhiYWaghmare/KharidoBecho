package com.spring.jwt.auction.mapper;

import com.spring.jwt.auction.dto.AuctionResponseDTO;
import com.spring.jwt.auction.entity.Auction;
import org.springframework.stereotype.Component;

@Component
public class AuctionMapper {
    public AuctionResponseDTO toDTO(Auction a) {
        if (a == null) return null;
        return AuctionResponseDTO.builder()
                .auctionId(a.getAuctionId())
                .listingId(a.getListingId())
                .startPrice(a.getStartPrice())
                .currentPrice(a.getCurrentPrice())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .build();
    }
}
