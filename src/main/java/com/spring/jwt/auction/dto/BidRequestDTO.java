package com.spring.jwt.auction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRequestDTO {
    @NotNull
    private Long auctionId;
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;
}
