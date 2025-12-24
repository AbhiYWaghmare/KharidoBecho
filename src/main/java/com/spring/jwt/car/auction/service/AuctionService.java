package com.spring.jwt.car.auction.service;

import com.spring.jwt.car.auction.dto.AuctionDTO;
import com.spring.jwt.car.auction.dto.AuctionUpdateMessageDTO;
import com.spring.jwt.car.auction.entity.Auction;
import com.spring.jwt.car.auction.carsocket.dto.ChatMessageDTO;

import java.math.BigDecimal;

public interface AuctionService {

    AuctionDTO createAuction(AuctionDTO dto);

    void startDueAuctions();

    void endDueAuctions();

    void processExpiredOffers();

    void winnerAccept(Long auctionId, Long userId);

    void winnerReject(Long auctionId, Long userId);
    void broadcastRunningAuctions();

    AuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
    Auction getAuctionById(Long auctionId);
    void saveChatMessage(Long bookingId, ChatMessageDTO incomingMsg);

}
