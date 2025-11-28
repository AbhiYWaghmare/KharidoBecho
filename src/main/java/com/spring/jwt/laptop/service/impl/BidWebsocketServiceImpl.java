package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.laptop.dto.AuctionDTO;
import com.spring.jwt.laptop.service.BidWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidWebsocketServiceImpl implements BidWebsocketService {


    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastAuctionUpdate(AuctionDTO update) {
        messagingTemplate.convertAndSend(
                "/topic/auction/" + update.getBeadingLaptopId(),
                update
        );
    }
}
