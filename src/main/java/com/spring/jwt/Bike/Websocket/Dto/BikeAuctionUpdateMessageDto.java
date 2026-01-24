//package com.spring.jwt.Bike.Websocket.Dto;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public record BikeAuctionUpdateMessageDto(
//        String type,
//        Long auctionId,
//        BigDecimal currentPrice,
//        Long highestBidderUserId,
//        List<TopBidDTO> topBids,
//        LocalDateTime offerExpiresAt
//) {
//    public record TopBidDTO(
//            int rank,
//            Long userId,
//            BigDecimal amount
//    ) {}
//}
//
