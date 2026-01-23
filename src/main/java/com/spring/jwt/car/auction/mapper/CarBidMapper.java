//package com.spring.jwt.car.auction.mapper;
//
//import com.spring.jwt.car.auction.dto.CarBidDTO;
//import com.spring.jwt.car.auction.entity.CarBid;
//
//public class CarBidMapper {
//
//    public static CarBidDTO toDTO(CarBid b) {
//        if (b == null) return null;
//
//        return new CarBidDTO(
//                b.getBidId(),
//                b.getAuction() != null ? b.getAuction().getAuctionId() : null,
//                b.getBidderUserId(),
//                b.getAmount(),
//                b.getStatus() != null ? b.getStatus().name() : null,
//                b.getCreatedAt()
//        );
//    }
//}
