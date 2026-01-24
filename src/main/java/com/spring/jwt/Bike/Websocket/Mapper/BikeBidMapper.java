//package com.spring.jwt.Bike.Websocket.Mapper;
//
//import com.spring.jwt.Bike.Websocket.Dto.BikeBidDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeBid;
//
//public class BikeBidMapper {
//
//    public static BikeBidDto toDto(BikeBid bid) {
//        return BikeBidDto.builder()
//                .bidId(bid.getBidId())
//                .auctionId(bid.getAuction().getAuctionId())
//                .bidderUserId(bid.getBidderUserId())
//                .amount(bid.getAmount())
//                .status(bid.getStatus().name())
//                .createdAt(bid.getCreatedAt())
//                .build();
//    }
//}
