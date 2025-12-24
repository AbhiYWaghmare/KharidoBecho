package com.spring.jwt.Bike.Websocket.Socket;

import com.spring.jwt.Bike.Service.Bike_booking_service;
import com.spring.jwt.Bike.Websocket.Socket.Dto.BikeBidMessageDto;
import com.spring.jwt.Bike.Websocket.Socket.Dto.BikeChatMessageDto;
import com.spring.jwt.Bike.Websocket.Socket.Dto.BikeEventDto;
import com.spring.jwt.Bike.Websocket.Service.BikeAuctionService;
import com.spring.jwt.Bike.dto.Bike_booking_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BikeAuctionWebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final BikeAuctionService bikeAuctionService;
    private final Bike_booking_service bikeBookingService;


// =========================
// Chat WebSocket
// =========================

@MessageMapping("/bike/chat/{bookingId}/send")
public void sendChat(
        @DestinationVariable Long bookingId,
        @Payload BikeChatMessageDto chatMessage) {

    //chatMessage.setBookingId(bookingId);

    bikeAuctionService.saveChatMessage(bookingId, chatMessage);

    messagingTemplate.convertAndSend(
            "/topic/bike/chat/" + bookingId, chatMessage
    );
}






    // =========================
    // Auction WebSocket (Place Bid)
    // =========================
    @MessageMapping("/bike/auction/{auctionId}/bid")
    public void placeBid(@DestinationVariable Long auctionId,
                         @Payload BikeBidMessageDto bidMessage) {

        Long userId = bidMessage.userId();
        BigDecimal amount = bidMessage.bidAmount();

        // Call the service to place the bid
        bikeAuctionService.placeBid(auctionId, userId, amount);
    }
}
