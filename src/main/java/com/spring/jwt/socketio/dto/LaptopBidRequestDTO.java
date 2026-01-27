package com.spring.jwt.socketio.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class LaptopBidRequestDTO {

    private Long auctionId;
    private Long bidderUserId;
    private BigDecimal amount;

}
