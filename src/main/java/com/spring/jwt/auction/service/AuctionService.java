package com.spring.jwt.auction.service;

import com.spring.jwt.auction.dto.AuctionDTO;

import java.math.BigDecimal;

public interface AuctionService {

    void placeBid(Long auctionId, Long userId, java.math.BigDecimal bidAmount);

    void startDueAuctions();

    void endDueAuctions();

    void processExpiredOffers();

    void winnerAccept(Long auctionId, Long userId);

    void winnerReject(Long auctionId, Long userId);

    AuctionDTO createAuction(AuctionDTO dto);
}
