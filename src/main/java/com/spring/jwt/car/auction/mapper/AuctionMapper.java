package com.spring.jwt.car.auction.mapper;

import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.auction.dto.AuctionDTO;
import com.spring.jwt.car.auction.entity.Auction;

public class AuctionMapper {

    // ============================
    //      ENTITY → DTO
    // ============================
    public static AuctionDTO toDTO(Auction a) {
        if (a == null) return null;

        Long carId = null;
        if (a.getCar() != null) {
            carId = a.getCar().getCarId();   // 👈 IMPORTANT: Car ID
        }

        return new AuctionDTO(
                a.getAuctionId(),
                carId,
                a.getStartPrice(),
                a.getCurrentPrice(),
                a.getMinIncrementInRupees(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getStartTime(),
                a.getEndTime(),
                a.getHighestBidderUserId()
        );
    }

    // ============================
    //      DTO → ENTITY
    // ============================
    public static Auction toEntity(AuctionDTO dto) {
        if (dto == null) return null;

        Auction a = new Auction();
        a.setAuctionId(dto.auctionId());

        // Create car stub (only ID)
        if (dto.carId() != null) {
            Car c = new Car();
            c.setCarId(dto.carId());
            a.setCar(c);
        }

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
