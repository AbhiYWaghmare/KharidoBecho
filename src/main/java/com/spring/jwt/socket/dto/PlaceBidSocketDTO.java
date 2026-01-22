package com.spring.jwt.socket.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlaceBidSocketDTO {
    private Long auctionId;
    private Long userId;
    private BigDecimal amount;
}
