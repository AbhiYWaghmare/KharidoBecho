//package com.spring.jwt.auction.mapper;
//
//import com.spring.jwt.Mobile.entity.Mobile;
//import com.spring.jwt.auction.dto.AuctionDTO;
//import com.spring.jwt.auction.entity.Auction;
//
//public class AuctionMapper {
//
//    public static AuctionDTO toDTO(Auction a) {
//        if (a == null) return null;
//
//        Long mobileId = null;
//        if (a.getMobile() != null) {
//            mobileId = a.getMobile().getMobileId();   //IMPORTANT
//        }
//
//        return new AuctionDTO(
//                a.getAuctionId(),
//                mobileId,
//                a.getStartPrice(),
//                a.getCurrentPrice(),
//                a.getMinIncrementInRupees(),
//                a.getStatus() != null ? a.getStatus().name() : null,
//                a.getStartTime(),
//                a.getEndTime(),
//                a.getHighestBidderUserId()
//        );
//    }
//
//
//    public static Auction toEntity(AuctionDTO dto) {
//        if (dto == null) return null;
//
//        Auction a = new Auction();
//        a.setAuctionId(dto.auctionId());
//
////        // create a stub Mobile with only ID set
////        if (dto.mobileId() != null) {
////            Mobile m = new Mobile();
////            m.setMobileId(dto.mobileId());
////            a.setMobile(m);
////        }
//
//        a.setStartPrice(dto.startPrice());
//        a.setCurrentPrice(dto.currentPrice());
//        a.setMinIncrementInRupees(dto.minIncrementInRupees());
//
//        if (dto.status() != null) {
//            a.setStatus(Auction.Status.valueOf(dto.status()));
//        }
//
//        a.setStartTime(dto.startTime());
//        a.setEndTime(dto.endTime());
//        a.setHighestBidderUserId(dto.highestBidderUserId());
//        return a;
//    }
//}
