package com.spring.jwt.socketio.service;

import com.spring.jwt.socketio.dto.LaptopBidRequestDTO;
import com.spring.jwt.socketio.dto.LaptopPlacedBidDTO;

import java.util.List;

public interface LaptopBidService {
    void placeBid(LaptopBidRequestDTO dto);

    List<LaptopPlacedBidDTO> getTopThreeBids(Long auctionId);

    LaptopPlacedBidDTO getTopBid(Long auctionId);


}
