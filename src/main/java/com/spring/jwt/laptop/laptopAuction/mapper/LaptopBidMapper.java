package com.spring.jwt.laptop.laptopAuction.mapper;

import com.spring.jwt.laptop.laptopAuction.dto.LaptopBidDTO;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopBid;

public class LaptopBidMapper {
    public static LaptopBidDTO toDTO(LaptopBid b) {
        if (b == null) return null;

        return new LaptopBidDTO(
                b.getBidId(),
                b.getAuction().getAuctionId(),
                b.getBidderUserId(),
                b.getAmount(),
                b.getStatus().name(),
                b.getCreatedAt()
        );
    }
}
