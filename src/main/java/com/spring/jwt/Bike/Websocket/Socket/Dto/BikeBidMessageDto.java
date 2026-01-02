package com.spring.jwt.Bike.Websocket.Socket.Dto;

import java.math.BigDecimal;

public record BikeBidMessageDto(
        Long userId,
        Long auctionId,
      //  BigDecimal bidAmount
        BigDecimal amount
) {}