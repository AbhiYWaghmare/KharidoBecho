//package com.spring.jwt.laptop.laptopAuction.laptopSocket.handler;
//
//import com.corundumstudio.socketio.SocketIOServer;
//import com.spring.jwt.laptop.laptopAuction.laptopSocket.dto.LaptopBidMessageDTO;
//import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Component
//public class AuctionSocketIOHandler {
//
//    public AuctionSocketIOHandler(SocketIOServer server,
//                                  LaptopAuctionService laptopAuctionService){
//
//        server.addConnectListener(client -> {
//            log.info("Client connected {}",client.getSessionId());
//        });
//
//        server.addEventListener("join_auction", Long.class,(client,auctionId,ack) -> {
//            client.joinRoom("auction_" +auctionId);
//            log.info("Client {} joined auction room {} " ,client.getSessionId(),auctionId);
//        });
//
//        server.addEventListener("new_bid", LaptopBidMessageDTO.class,(client,bid,ack)->{
//            Long auctionId = bid.auctionId();
//            Long userId = bid.userId();
//            var amount = bid.bidAmount();
//
//            log.info("New Bid: auction={},user={},amount={}",auctionId,userId,amount);
//
//            laptopAuctionService.placeBid(auctionId,userId,amount);
//
//            server.getRoomOperations("auction_" +auctionId)
//                    .sendEvent("bid_update",bid);
//        });
//
//        server.addDisconnectListener(client ->
//                log.info("Client Disconnected: {}",client.getSessionId()));
//    }
//}
