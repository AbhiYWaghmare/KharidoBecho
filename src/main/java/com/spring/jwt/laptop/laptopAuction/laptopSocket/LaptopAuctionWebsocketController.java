//package com.spring.jwt.laptop.laptopAuction.laptopSocket;
//
//import com.spring.jwt.laptop.dto.LaptopRequestResponseDTO;
//import com.spring.jwt.laptop.laptopAuction.laptopSocket.dto.LaptopBidMessageDTO;
//import com.spring.jwt.laptop.laptopAuction.laptopSocket.dto.LaptopChatMessageDTO;
//import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
//import com.spring.jwt.laptop.service.LaptopRequestService;
//import lombok.AllArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.math.BigDecimal;
//
//@AllArgsConstructor
//@Controller
//public class LaptopAuctionWebsocketController {
//    private final LaptopRequestService laptopRequestService;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final LaptopAuctionService auctionService;
//
//    // chat ws
//    @MessageMapping("/laptop/chat/{requestId}/send")
//    public void sendChat(@DestinationVariable Long requestId,
//                         @Payload LaptopChatMessageDTO chatMessage) {
//
//        Long senderUserId = chatMessage.getUserId();
//        String text = chatMessage.getMessage();
//
//        LaptopRequestResponseDTO updated =
//                laptopRequestService.appendMessage(requestId, senderUserId, text);
//
//        messagingTemplate.convertAndSend("/topic/laptop/chat/" + requestId, updated);
//    }
//
//    // auction ws
//    @MessageMapping("/laptop/auction/{auctionId}/bid")
//    public void placeBid(@DestinationVariable Long auctionId,
//                         @Payload LaptopBidMessageDTO bidMessage) {
//
//        Long userId = bidMessage.userId();
//        BigDecimal amount = bidMessage.bidAmount();
//
//        auctionService.placeBid(auctionId, userId, amount);
//    }
//}
