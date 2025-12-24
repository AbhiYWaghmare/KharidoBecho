package com.spring.jwt.socket;

import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.Mobile.dto.MobileRequestResponseDTO;
import com.spring.jwt.auction.service.AuctionService;
import com.spring.jwt.socket.dto.BidMessageDTO;
import com.spring.jwt.socket.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

    private final MobileRequestService mobileRequestService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionService auctionService;

    // CHAT using WS
    // Client sends to: /app/chat/{requestId}/send
    @MessageMapping("/chat/{requestId}/send")
    public void sendChat(@DestinationVariable Long requestId,
                         @Payload ChatMessageDTO chatMessage) {

        Long senderUserId = chatMessage.getUserId();
        String text = chatMessage.getMessage();

        // 1 persist message using your existing logic
        MobileRequestResponseDTO updated = mobileRequestService.appendMessage(requestId, senderUserId, text);

        // 2 broadcast updated conversation to all subscribers
        messagingTemplate.convertAndSend("/topic/chat/" + requestId, updated);
    }

//    @MessageMapping("/auction/{auctionId}/bid")
//    public void placeBid(@DestinationVariable Long auctionId,
//                         @Payload BidMessageDTO bidMessage) {
//
//        Long userId = bidMessage.userId();  //   from message, not from JWT
//        if (userId == null) {
//            throw new IllegalArgumentException("userId is required in bidMessage");
//        }
//
//        auctionService.placeBid(auctionId, userId, bidMessage.bidAmount());
//    }
    // you can keep your bid/auction mappings here too if you want

    // Client sends: /app/auction/{auctionId}/bid


    @MessageMapping("/auction/{auctionId}/bid")
    public void placeBid(@DestinationVariable Long auctionId,
                         @Payload BidMessageDTO bidMessage) {

        System.out.println("🔥 Received WS bid: auctionId=" + auctionId +
                ", userId=" + bidMessage.userId() +
                ", amount=" + bidMessage.bidAmount());


        // In current no-auth WS mode we take userId from payload
        Long userId = bidMessage.userId();
        BigDecimal amount = bidMessage.bidAmount();

        auctionService.placeBid(auctionId, userId, amount);
    }

}
