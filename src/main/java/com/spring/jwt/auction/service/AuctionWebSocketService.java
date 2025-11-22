package com.spring.jwt.auction.service;

import com.spring.jwt.auction.dto.AuctionResponseDTO;
import com.spring.jwt.auction.dto.OfferDTO;
import com.spring.jwt.auction.entity.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuctionWebSocketService {

    private final SimpMessagingTemplate messaging;

    public void broadcastAuctionStarted(Auction a) {
        messaging.convertAndSend("/topic/auction/" + a.getListingId(),
                Map.of("type","AUCTION_STARTED","auctionId", a.getAuctionId(), "startPrice", a.getStartPrice(), "endTime", a.getEndTime()));
    }

    public void broadcastNewHighBid(Auction a, Map<String, Object> payload) {
        messaging.convertAndSend("/topic/auction/" + a.getListingId(), payload);
    }

    // send a private offer message to a specific user
    public void sendOfferToUser(Long userId, OfferDTO offer) {
        messaging.convertAndSendToUser(String.valueOf(userId), "/queue/auction", offer);
    }

    public void broadcastAuctionEnded(Auction a, Map<String,Object> payload) {
        messaging.convertAndSend("/topic/auction/" + a.getListingId(), payload);
    }
}
