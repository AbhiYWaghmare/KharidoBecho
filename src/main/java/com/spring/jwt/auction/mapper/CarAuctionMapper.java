package com.spring.jwt.auction.mapper;

import com.spring.jwt.auction.dto.CarAuctionDTO;
import com.spring.jwt.auction.entity.CarAuction;

public class CarAuctionMapper {

    public static CarAuctionDTO toDTO(CarAuction a) {
        if (a == null) return null;
        return new CarAuctionDTO(
                a.getAuctionId(),
                a.getCarId(),
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
