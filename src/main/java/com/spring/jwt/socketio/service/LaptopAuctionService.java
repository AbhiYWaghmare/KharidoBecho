package com.spring.jwt.socketio.service;

import com.spring.jwt.socketio.dto.LaptopAuctionCreateDTO;
import com.spring.jwt.socketio.dto.LaptopAuctionResponseDTO;

import java.util.List;

public interface LaptopAuctionService {

    LaptopAuctionResponseDTO createAuction(LaptopAuctionCreateDTO dto);


}
