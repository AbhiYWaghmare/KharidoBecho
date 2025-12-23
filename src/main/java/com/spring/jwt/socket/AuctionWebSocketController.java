
package com.spring.jwt.socket;

import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.car.auction.service.AuctionService;
import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.services.CarBookingService;
import com.spring.jwt.socket.dto.AuctionEventDTO;
import com.spring.jwt.socket.dto.BidMessageDTO;
import com.spring.jwt.socket.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

    private final CarBookingService carBookingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionService auctionService;

    // CHAT using WS
    // Client sends to: /app/chat/{requestId}/send
    @MessageMapping("/auction/{auctionId}/bid")
    public void placeBid(@DestinationVariable Long auctionId,
                         @Payload BidMessageDTO bidMessage) {

        Long userId = bidMessage.userId();
        BigDecimal amount = bidMessage.bidAmount();

        System.out.println("🔥 WS Bid Received: auction=" + auctionId
                + " user=" + userId + " amount=" + amount);

        // place bid
        auctionService.placeBid(auctionId, userId, amount);

        // broadcast update
        AuctionEventDTO update = new AuctionEventDTO(
                "BID_PLACED",
                auctionId,
                null,
                amount,
                userId,
                OffsetDateTime.now(),
                null
        );

        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, update);
    }

    @MessageMapping("/car/chat/{bookingId}/send")
    public void sendChat(@DestinationVariable Long bookingId,
                         @Payload ChatMessageDTO chatMessage) {

        Long userId = chatMessage.getUserId();
        String message = chatMessage.getMessage();

        CarBooking updated =
                carBookingService.addMessage(bookingId, userId, message);

        messagingTemplate.convertAndSend(
                "/topic/car/chat/" + bookingId,
                updated
        );
    }


}

