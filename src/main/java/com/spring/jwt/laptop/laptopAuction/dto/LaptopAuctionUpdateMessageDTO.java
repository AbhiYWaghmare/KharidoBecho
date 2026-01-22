//package com.spring.jwt.laptop.laptopAuction.dto;
//
//import com.spring.jwt.auction.dto.AuctionUpdateMessageDTO;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public record LaptopAuctionUpdateMessageDTO(
//        String type,
//        Long auctionId,
//        BigDecimal currentPrice,
//        Long highestBidderUserId,
//        List<LaptopAuctionUpdateMessageDTO.TopBidDTO> topBids,
//        LocalDateTime offerExpiresAt
//) {
//        public record TopBidDTO(
//                int rank,
//                Long userId,
//                BigDecimal amount
//        ) {}
//}
