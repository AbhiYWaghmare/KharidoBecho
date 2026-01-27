package com.spring.jwt.socketio.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.spring.jwt.socketio.dto.LaptopBidRequestDTO;
import com.spring.jwt.socketio.dto.LaptopPlacedBidDTO;
import com.spring.jwt.socketio.entity.LaptopAuction;
import com.spring.jwt.socketio.repository.LaptopAuctionRepository;
import com.spring.jwt.socketio.service.LaptopBidService;
import com.spring.jwt.socketio.service.SocketIOService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LaptopBidSocketController {
    private final LaptopBidService laptopBidService;
    private final SocketIOService socketIOService;
    private final LaptopAuctionRepository auctionRepo;


    // ================= CONNECT =================
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Socket connected: {}", client.getSessionId());
    }

    // ================= DISCONNECT =================
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Socket disconnected: {}", client.getSessionId());
    }

    // ================= JOIN AUCTION =================
    @OnEvent("joinAuction")
    public void onJoinAuction(SocketIOClient client, Long auctionId) {

        if (auctionId == null) {
            client.sendEvent("error", "Auction ID is required");
            return;
        }

        String room = "auction_" + auctionId;
        socketIOService.joinRoom(client, room);

        log.info("Client {} joined {}", client.getSessionId(), room);

        client.sendEvent(
                "joinAuctionResponse",
                "Joined auction " + auctionId
        );

        // Send current top bid + top 3 immediately
        client.sendEvent(
                "laptopTopBid",
                laptopBidService.getTopBid(auctionId)
        );

        client.sendEvent(
                "laptopTopThreeBids",
                laptopBidService.getTopThreeBids(auctionId)
        );
    }

    // ================= PLACE BID =================
    @OnEvent("placeLaptopBid")
    public void onPlaceBid(SocketIOClient client,
                           Map<String, Object> data) {

        try {
            Long auctionId = Long.valueOf(data.get("auctionId").toString());
            Long bidderUserId = Long.valueOf(data.get("bidderUserId").toString());

            Object raw = data.get("amount");
            BigDecimal amount;

            if (raw instanceof Number) {
                amount = BigDecimal.valueOf(((Number) raw).doubleValue());
            } else {
                amount = new BigDecimal(raw.toString().trim());
            }

            LaptopBidRequestDTO dto = new LaptopBidRequestDTO();
            dto.setAuctionId(auctionId);
            dto.setBidderUserId(bidderUserId);
            dto.setAmount(amount);

            log.info("FINAL BID DTO = {}", dto);

            laptopBidService.placeBid(dto);

            client.sendEvent("placeLaptopBidResponse", "Bid placed successfully");

            String room = "auction_" + auctionId;

            socketIOService.sendToRoom(
                    room,
                    "laptopTopBid",
                    laptopBidService.getTopBid(auctionId)
            );

            socketIOService.sendToRoom(
                    room,
                    "laptopTopThreeBids",
                    laptopBidService.getTopThreeBids(auctionId)
            );

        } catch (Exception ex) {
            log.error("Bid placement failed", ex);
            client.sendEvent("placeLaptopBidResponse", ex.getMessage());
        }
    }

    @OnEvent("getLiveAuctions")
    public void getLiveAuctions(SocketIOClient client) {

        var live = auctionRepo.findByStatus(LaptopAuction.AuctionStatus.RUNNING);

        List<Map<String, Object>> data = live.stream().map(a -> {
            Map<String, Object> m = new HashMap<>();
            m.put("auctionId", a.getAuctionId());
            m.put("currentPrice", a.getCurrentPrice());
            m.put("highestBidder", a.getHighestBidderUserId());
            return m;
        }).toList();


        client.sendEvent("liveAuctions", data);
    }




    // ================= LEAVE AUCTION =================
    @OnEvent("leaveAuction")
    public void onLeaveAuction(SocketIOClient client, Long auctionId) {

        if (auctionId == null) {
            return;
        }

        String room = "auction_" + auctionId;
        socketIOService.leaveRoom(client, room);

        log.info("Client {} left {}", client.getSessionId(), room);

        client.sendEvent(
                "leaveAuctionResponse",
                "Left auction " + auctionId
        );
    }

}
