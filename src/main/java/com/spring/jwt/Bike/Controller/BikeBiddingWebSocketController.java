package com.spring.jwt.Bike.Controller;


import com.spring.jwt.Bike.Service.BikeAuctionService;
import com.spring.jwt.Bike.dto.AuctionDto;
import org.springframework.stereotype.Controller;


import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
@RequiredArgsConstructor
public class BikeBiddingWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
   private final BikeAuctionService bikeAuctionService;
//
//    // Client sends bid → /app/bid
//    // Server broadcasts → /topic/bids
//    @MessageMapping("/bid")
//    @SendTo("/topic/bids")
//    public BikeBidResponceDto placeBid(BikeBidDto bidDto) {
//        return bikeAuctionService.placeBid(
//                bidDto.getAuctionId(),
//                bidDto.getUserId(),
//                bidDto.getBidAmount()
//        );
//    }
//
//    // Send live auction details
//    @MessageMapping("/auction/{auctionId}")
//    @SendTo("/topic/auction/{auctionId}")
//    public BikeBidding getLiveAuction(@DestinationVariable Long auctionId) {
//
//        return bikeAuctionService.getAuctionById(auctionId);
//    }
//}
    /*
@MessageMapping("/bid")      // client sends here → /app/bid
@SendTo("/topic/bids")       // broadcast to all users
public AuctionDto processLiveBids(AuctionDto bid) {

    // Process bid using service
    return bikeAuctionService.processLiveBid(bid);
}

     */
@MessageMapping("/bid")        // client → /app/bid
@SendTo("/topic/bids")         // broadcast → /topic/bids
public AuctionDto processLiveBids(AuctionDto dto) {
    return bikeAuctionService.processLiveBid(dto);
}
}
