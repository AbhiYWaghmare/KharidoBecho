package com.spring.jwt.socket;

import com.spring.jwt.socket.dto.BidMessageDTO;
import com.spring.jwt.socket.dto.ChatMessageDTO;
import com.spring.jwt.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

    private final AuctionService auctionService;

    // Client sends to /app/auction/{auctionId}/bid
//    @MessageMapping("/auction/{auctionId}/bid")
//    public void placeBid(@DestinationVariable Long auctionId,
//                         @Payload BidMessageDTO bidMessage,
//                         SimpMessageHeaderAccessor headerAccessor) {
//
//        // extract authenticated user id from handshake attributes (set earlier)
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//
//        if (userId == null) throw new IllegalArgumentException("Unauthenticated");
//
//        auctionService.placeBid(auctionId, userId, bidMessage.bidAmount());
//    }

    @MessageMapping("/auction/{auctionId}/bid")
    public void placeBid(@DestinationVariable Long auctionId,
                         @Payload BidMessageDTO bidMessage) {

        Long userId = bidMessage.userId();  // 👈 from message, not from JWT
        if (userId == null) {
            throw new IllegalArgumentException("userId is required in bidMessage");
        }

        auctionService.placeBid(auctionId, userId, bidMessage.bidAmount());
    }


    // Chat message mapping: client sends to /app/chat/{requestId}/send
//    @MessageMapping("/chat/{requestId}/send")
//    public void sendChat(@DestinationVariable Long requestId,
//                         @Payload ChatMessageDTO chatMessage,
//                         SimpMessageHeaderAccessor headerAccessor) {
//        // Persist message using your MobileRequestService.appendMessage logic,
//        // then broadcast to /topic/chat/{requestId}.
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//
//        // call your service to append and persist
//        // mobileRequestService.appendMessageByWs(requestId, userId, chatMessage.message());
//
//        // broadcast (example) - include timestamp etc.
//        // messagingTemplate.convertAndSend("/topic/chat/" + requestId, chatMessage);
//    }

    @MessageMapping("/chat/{requestId}/send")
    public void sendChat(@DestinationVariable Long requestId,
                         @Payload ChatMessageDTO chatMessage) {

        Long userId = chatMessage.userId();
        if (userId == null) {
            throw new IllegalArgumentException("userId is required in chatMessage");
        }

        // mobileRequestService.appendMessage(requestId, userId, chatMessage.message());
        // messagingTemplate.convertAndSend("/topic/chat/" + requestId, chatMessage);
    }

}
