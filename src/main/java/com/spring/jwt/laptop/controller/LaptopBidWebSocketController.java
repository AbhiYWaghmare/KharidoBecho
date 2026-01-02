//package com.spring.jwt.laptop.controller;
//
//import com.spring.jwt.laptop.dto.AuctionDTO;
//import com.spring.jwt.laptop.dto.BeadingLaptopWithInsDTO;
//import com.spring.jwt.laptop.dto.BidLaptopDTO;
//import com.spring.jwt.laptop.dto.BidResponseDTO;
//import com.spring.jwt.laptop.entity.BidLaptops;
//import com.spring.jwt.laptop.repository.BidLaptopRepository;
//import com.spring.jwt.laptop.service.LaptopBeadingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class LaptopBidWebSocketController {
//
//    private final LaptopBeadingService laptopBeadingService;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final BidLaptopRepository bidLaptopRepo;
//
//    @MessageMapping("/auction/{auctionId}/bid/bid")
//    public void placeBid(
//            @DestinationVariable Long auctionId,
//            BidLaptopDTO bidMessage
//    ) {
//        bidMessage.setBeadingLaptopId(auctionId); // AuctionId comes from URL
//
//        BidResponseDTO response = laptopBeadingService.placeBid(bidMessage);
//
//        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, response);
//    }
//
//
//
////    @MessageMapping("/auction/bid")
////    public void placeBid(BidLaptopDTO bid) {
////
////        // Process the bid
////        BidResponseDTO response = laptopBeadingService.placeBid(bid);
////
////        // Broadcast update
////        messagingTemplate.convertAndSend(
////                "/topic/auction/" + bid.getBeadingLaptopId(),
////                response
////        );
////    }
//
//    @MessageMapping("/auctions/live")
//    @SendTo("/topic/auctions/live")
//    public List<BidLaptops> getAllLiveAuctionsWS() {
//        LocalDateTime now = LocalDateTime.now();
//        return bidLaptopRepo.findByClosingAtAfter(now);  // ✔ Only live bids
//    }
//
//}
