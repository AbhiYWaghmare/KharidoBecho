//package com.spring.jwt.Bike.Websocket.Service;
//
//
//import com.spring.jwt.Bike.Websocket.Dto.CreateBikeAuctionRequestDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeAuction;
//import com.spring.jwt.Bike.Websocket.Dto.BikeBidDto;
//import com.spring.jwt.Bike.Websocket.Socket.Dto.BikeChatMessageDto;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public interface BikeAuctionService {
//
//    BikeBidDto placeBid(Long auctionId, Long userId, BigDecimal amount);
//
//    //BikeAuction createAuction(BikeAuction dto);
//    BikeAuction createAuction(CreateBikeAuctionRequestDto dto);
//
//
//    void startDueAuctions();
//
//    void endDueAuctions();
//
//    void processExpiredOffers();
//
//    void winnerAccept(Long auctionId, Long userId);
//
//    void winnerReject(Long auctionId, Long userId);
//    BikeAuction getAuctionById(Long id);
//
//    List<BikeAuction> listAuctionsByStatus(String status);
//    void saveChatMessage(Long bookingId, BikeChatMessageDto message);
//
//}
//
