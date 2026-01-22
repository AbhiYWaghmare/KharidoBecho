//
//package com.spring.jwt.car.auction.carsocket;
//
//import com.spring.jwt.car.auction.service.CarAuctionService;
//import com.spring.jwt.car.services.CarBookingService;
//import com.spring.jwt.car.auction.carsocket.dto.CarEventDTO;
//import com.spring.jwt.car.auction.carsocket.dto.CarBidMessageDTO;
//import com.spring.jwt.car.auction.carsocket.dto.CarChatMessageDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.math.BigDecimal;
//import java.time.OffsetDateTime;
//
//@Controller
//@RequiredArgsConstructor
//public class CarWebSocketController {
//
//    private final CarBookingService carBookingService;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final CarAuctionService auctionService;
//
//    // CHAT using WS
//    // Client sends to: /app/chat/{requestId}/send
//    @MessageMapping("/car-auction/{auctionId}/bid")
//    public void placeBid(@DestinationVariable Long auctionId,
//                         @Payload CarBidMessageDTO bidMessage) {
//
//        Long userId = bidMessage.userId();
//        BigDecimal amount = bidMessage.bidAmount();
//
//        System.out.println(" WS Bid Received: auction=" + auctionId
//                + " user=" + userId + " amount=" + amount);
//
//        // place bid
//        auctionService.placeBid(auctionId, userId, amount);
//
//        // broadcast update
//        CarEventDTO update = new CarEventDTO(
//                "BID_PLACED",
//                auctionId,
//                null,
//                amount,
//                userId,
//                OffsetDateTime.now(),
//                null
//        );
//
//        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, update);
//    }
//
//    //    @MessageMapping("/car/chat/{bookingId}/send")
////    public void sendChat(@DestinationVariable Long bookingId,
////                         @Payload CarBookingDTO chatMessage) {
////
////        Long senderUserId = chatMessage.getUserId();
////        String text = chatMessage.getMessage();
////
////        // SAVE CHAT MESSAGE IN DATABASE
////        CarBooking updatedConversation =
////                carBookingService.addMessage(bookingId, chatMessage);
////
////        // SEND UPDATED CHAT TO ALL SUBSCRIBERS
////        messagingTemplate.convertAndSend(
////                "/topic/car/chat/" + bookingId,
////                updatedConversation
////        );
////
////    }
//    @MessageMapping("/chat/{bookingId}/send")
//    public void sendChat(@DestinationVariable Long bookingId,
//                         @Payload CarChatMessageDTO chatMessage) {
//
//        // DB save
//        auctionService.saveChatMessage(bookingId, chatMessage);
//
//        // broadcast updated conversation
//        messagingTemplate.convertAndSend(
//                "/topic/chat/" + bookingId,
//                chatMessage
//        );
//    }
//
//}
