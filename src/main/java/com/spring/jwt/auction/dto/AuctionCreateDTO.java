package com.spring.jwt.auction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuctionCreateDTO {
    @NotNull(message = "listingId required")
    private Long listingId;

    @NotNull(message = "startPrice required")
    @DecimalMin(value = "1.00", message = "startPrice must be >= 1")
    private BigDecimal startPrice;

    @Min(value = 1, message = "duration must be >= 1 minute")
    private int durationMinutes = 30;
}
