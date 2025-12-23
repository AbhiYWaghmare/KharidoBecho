<<<<<<< HEAD

package com.spring.jwt.socket;

import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.car.auction.service.AuctionService;
import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.services.CarBookingService;
import com.spring.jwt.socket.dto.AuctionEventDTO;
=======
package com.spring.jwt.socket;

import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.Mobile.dto.MobileRequestResponseDTO;
import com.spring.jwt.auction.service.AuctionService;
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
import com.spring.jwt.socket.dto.BidMessageDTO;
import com.spring.jwt.socket.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
<<<<<<< HEAD
import java.time.OffsetDateTime;
=======
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

<<<<<<< HEAD
    private final CarBookingService carBookingService;
=======
    private final MobileRequestService mobileRequestService;
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionService auctionService;

    // CHAT using WS
    // Client sends to: /app/chat/{requestId}/send
<<<<<<< HEAD
=======
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


>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
    @MessageMapping("/auction/{auctionId}/bid")
    public void placeBid(@DestinationVariable Long auctionId,
                         @Payload BidMessageDTO bidMessage) {

<<<<<<< HEAD
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

=======
        System.out.println("🔥 Received WS bid: auctionId=" + auctionId +
                ", userId=" + bidMessage.userId() +
                ", amount=" + bidMessage.bidAmount());


        // In current no-auth WS mode we take userId from payload
        Long userId = bidMessage.userId();
        BigDecimal amount = bidMessage.bidAmount();

        auctionService.placeBid(auctionId, userId, amount);
    }

}
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
