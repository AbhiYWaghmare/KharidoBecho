//package com.spring.jwt.auction.controller;
//
//import com.spring.jwt.auction.dto.BidRequestDTO;
//import com.spring.jwt.auction.service.AuctionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.*;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//
//@Controller
//@RequiredArgsConstructor
//public class AuctionWebSocketController {
//
//    private final AuctionService auctionService;
//
//    // Client sends a bid to /app/auction/{auctionId}/bid
//    @MessageMapping("/auction/{auctionId}/bid")
//    public void handleBid(@DestinationVariable Long auctionId,
//                          @Payload BidRequestDTO payload,
//                          SimpMessageHeaderAccessor headers) {
//        // obtain userId set by handshake
//        Long userId = (Long) headers.getSessionAttributes().get("userId");
//        if (userId == null) {
//            throw new IllegalArgumentException("Unauthenticated websocket user");
//        }
//        auctionService.placeBid(auctionId, userId, payload.getAmount());
//    }
//}
