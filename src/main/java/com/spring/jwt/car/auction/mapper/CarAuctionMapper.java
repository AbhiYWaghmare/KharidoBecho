//package com.spring.jwt.car.auction.mapper;
//
//import com.spring.jwt.car.entity.Car;
//import com.spring.jwt.car.auction.dto.CarAuctionDTO;
//import com.spring.jwt.car.auction.entity.CarAuction;
//
//public class CarAuctionMapper {
//
//    // ============================
//    //      ENTITY → DTO
//    // ============================
//    public static CarAuctionDTO toDTO(CarAuction a) {
//        if (a == null) return null;
//
//        Long carId = null;
//        if (a.getCar() != null) {
//            carId = a.getCar().getCarId();   // 👈 IMPORTANT: Car ID
//        }
//
//        return new CarAuctionDTO(
//                a.getAuctionId(),
//                carId,
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
//    // ============================
//    //      DTO → ENTITY
//    // ============================
//    public static CarAuction toEntity(CarAuctionDTO dto) {
//        if (dto == null) return null;
//
//        CarAuction a = new CarAuction();
//        a.setAuctionId(dto.auctionId());
//
//        // Create car stub (only ID)
//        if (dto.carId() != null) {
//            Car c = new Car();
//            c.setCarId(dto.carId());
//            a.setCar(c);
//        }
//
//        a.setStartPrice(dto.startPrice());
//        a.setCurrentPrice(dto.currentPrice());
//        a.setMinIncrementInRupees(dto.minIncrementInRupees());
//
//        if (dto.status() != null) {
//            a.setStatus(CarAuction.Status.valueOf(dto.status()));
//        }
//
//        a.setStartTime(dto.startTime());
//        a.setEndTime(dto.endTime());
//        a.setHighestBidderUserId(dto.highestBidderUserId());
//
//        return a;
//    }
//}
