package com.spring.jwt.car.auction.carsocket.dto;

import java.math.BigDecimal;

public record CarBidMessageDTO(
        Long userId,        //  add this
        Long auctionId,     // optional, or use path variable
        BigDecimal bidAmount
) {}
