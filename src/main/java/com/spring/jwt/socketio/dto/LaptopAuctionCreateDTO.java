package com.spring.jwt.socketio.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LaptopAuctionCreateDTO {
    private Long laptopId;
    private BigDecimal startPrice;
    private BigDecimal minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
