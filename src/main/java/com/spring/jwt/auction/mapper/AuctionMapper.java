package com.spring.jwt.auction.mapper;

import com.spring.jwt.auction.dto.AuctionDTO;
import com.spring.jwt.auction.entity.Auction;

public class AuctionMapper {

    public static AuctionDTO toDTO(Auction a) {
        if (a == null) return null;

        return new AuctionDTO(
                a.getAuctionId(),
                a.getMobileId(),
                a.getStartPrice(),
                a.getCurrentPrice(),
                a.getMinIncrementInRupees(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getStartTime(),
                a.getEndTime(),
                a.getHighestBidderUserId()
        );
    }

    public static Auction toEntity(AuctionDTO dto) {
        if (dto == null) return null;

        Auction a = new Auction();
        a.setAuctionId(dto.auctionId());
        a.setMobileId(dto.mobileId());
        a.setStartPrice(dto.startPrice());
        a.setCurrentPrice(dto.currentPrice());
        a.setMinIncrementInRupees(dto.minIncrementInRupees());
        if (dto.status() != null) {
            a.setStatus(Auction.Status.valueOf(dto.status()));
        }
        a.setStartTime(dto.startTime());
        a.setEndTime(dto.endTime());
        a.setHighestBidderUserId(dto.highestBidderUserId());
        return a;
    }
}
