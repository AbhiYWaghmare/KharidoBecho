package com.spring.jwt.Bike.Websocket.Socket.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BikeEventDto(
        String type,          //  "BID PLACED", "WINNER ACCEPTED", "AUCTION ENDED"
        Long auctionId,
        Long bikeId,
        BigDecimal amount,    // current bid amount
        Long bidderUserId,    // who made the bid
        LocalDateTime timestamp,
        Object extra          // any extra info
) {}
