package com.spring.jwt.laptop.laptopAuction.mapper;

import com.spring.jwt.laptop.laptopAuction.dto.LaptopAuctionDTO;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;

public class LaptopAuctionMapper {
    public static LaptopAuctionDTO toDTO(LaptopAuction a) {
        if (a == null) return null;

        return new LaptopAuctionDTO(
                a.getAuctionId(),
                a.getLaptopId(),
                a.getStartPrice(),
                a.getCurrentPrice(),
                a.getMinIncrementInRupees(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getStartTime(),
                a.getEndTime(),
                a.getHighestBidderUserId()
        );
    }
}
