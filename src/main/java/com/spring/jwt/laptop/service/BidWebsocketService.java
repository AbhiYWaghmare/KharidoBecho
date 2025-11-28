package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.dto.AuctionDTO;

public interface BidWebsocketService {
    public void broadcastAuctionUpdate(AuctionDTO update);
}
