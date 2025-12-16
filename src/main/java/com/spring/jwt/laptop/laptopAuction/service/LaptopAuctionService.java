package com.spring.jwt.laptop.laptopAuction.service;

import com.spring.jwt.laptop.laptopAuction.dto.LaptopAuctionDTO;

import java.math.BigDecimal;
import java.util.List;

public interface LaptopAuctionService {
    LaptopAuctionDTO createAuction(LaptopAuctionDTO dto);
    void placeBid(Long auctionId, Long userId, BigDecimal bidAmount);

    void startDueAuctions();

    void endDueAuctions();

    void processExpiredOffers();

    void winnerAccept(Long auctionId, Long userId);

    void winnerReject(Long auctionId, Long userId);

    LaptopAuctionDTO getById(Long id);
    List<LaptopAuctionDTO> listByStatus(String status);

    void broadcastRunningAuctions();
}
