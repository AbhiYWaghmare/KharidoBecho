package com.spring.jwt.car.auction.service;

import com.spring.jwt.car.auction.carsocket.dto.CarChatMessageDTO;
import com.spring.jwt.car.auction.dto.CarAuctionDTO;
import com.spring.jwt.car.auction.dto.CarAuctionUpdateMessageDTO;
import com.spring.jwt.car.auction.entity.CarAuction;
//import com.spring.jwt.car.auction.carsocket.dto.

import java.math.BigDecimal;

public interface CarAuctionService {

    CarAuctionDTO createAuction(CarAuctionDTO dto);

    void startDueAuctions();


    void endDueAuctions();

    void processExpiredOffers();

    void winnerAccept(Long auctionId, Long userId);

    void winnerReject(Long auctionId, Long userId);
    void broadcastRunningAuctions();

    CarAuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
    CarAuction getAuctionById(Long auctionId);
    void saveChatMessage(Long bookingId, CarChatMessageDTO incomingMsg);

//    AuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
//    Auction getAuctionById(Long auctionId);

}
