package com.spring.jwt.car.auction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AuctionDTO(

        Long auctionId,

        @NotNull(message = "Car ID is required")
        Long carId,

        @NotNull(message = "Start price is required")
        @DecimalMin(value = "1.0", inclusive = true,
                message = "Start price must be at least 1")
        BigDecimal startPrice,

        @NotNull(message = "Current price is required")
        @DecimalMin(value = "1.0", inclusive = true,
                message = "Current price must be at least 1")
        BigDecimal currentPrice,

        @NotNull(message = "Minimum increment is required")
        @DecimalMin(value = "1.0", inclusive = true,
                message = "Minimum increment must be at least 1")
        BigDecimal minIncrementInRupees,

        String status,

        @NotNull(message = "Start time is required")
        @FutureOrPresent(message = "Start time cannot be in the past")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @FutureOrPresent(message = "End time cannot be in the past")
        LocalDateTime endTime,

        Long highestBidderUserId
) {}
