package com.spring.jwt.Bike.Websocket.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder

public record BikeAuctionDto(
        Long auctionId,
        @NotNull(message = "bike_ID is required")
        Long bikeId,
       // Long bike_id,
        @NotNull(message = "Start price is required")
        @DecimalMin(value = "1.0", message = "Start price must be greater than 0")
        BigDecimal startPrice,
        @NotNull(message = "Current price is required")
        @DecimalMin(value = "1.0", message = "Current price must be greater than 0")
        BigDecimal currentPrice,
        @NotNull(message = "Minimum increment is required")
        @DecimalMin(value = "1.0", message = "Minimum increment must be greater than 0")
        BigDecimal minIncrement,
        String status,
        @NotNull(message = "Start time is required")
        @FutureOrPresent(message = "Start time cannot be in the past")
        LocalDateTime startTime,
        @NotNull(message = "End time is required")
//        @FutureOrPresent(message = "End time cannot be in the past")
        LocalDateTime endTime,
        Long highestBidderUserId
) {}

