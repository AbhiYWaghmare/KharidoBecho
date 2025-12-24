package com.spring.jwt.car.auction.service;

import com.spring.jwt.car.auction.dto.AuctionDTO;
import com.spring.jwt.car.auction.dto.AuctionUpdateMessageDTO;
import com.spring.jwt.car.auction.entity.Auction;
<<<<<<< HEAD
import com.spring.jwt.car.auction.carsocket.dto.ChatMessageDTO;
=======
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d

import java.math.BigDecimal;

public interface AuctionService {

    AuctionDTO createAuction(AuctionDTO dto);

    void startDueAuctions();

    void endDueAuctions();

    void processExpiredOffers();

    void winnerAccept(Long auctionId, Long userId);

    void winnerReject(Long auctionId, Long userId);
<<<<<<< HEAD
    void broadcastRunningAuctions();

    AuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
    Auction getAuctionById(Long auctionId);
    void saveChatMessage(Long bookingId, ChatMessageDTO incomingMsg);
=======

    AuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
    Auction getAuctionById(Long auctionId);
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d

}
