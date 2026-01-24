//package com.spring.jwt.Bike.Websocket.Dto;
//
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.Future;
//import jakarta.validation.constraints.FutureOrPresent;
//import jakarta.validation.constraints.NotNull;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//public record CreateBikeAuctionRequestDto(
//        @NotNull(message = "bike_ID is required")
//        Long bikeId,
//        @NotNull(message = "Start price is required")
//        @DecimalMin(value = "1.0", message = "Start price must be greater than 0")
//        BigDecimal startPrice,
//        @NotNull(message = "Minimum increment is required")
//        @DecimalMin(value = "1.0", message = "Minimum increment must be greater than 0")
//        BigDecimal minIncrement,
//        @NotNull(message = "Start time is required")
//        @FutureOrPresent(message = "Start time cannot be in the past")
//        LocalDateTime startTime,
//        @NotNull(message = "End time is required")
//        @Future(message = "End time must be in future ")
//        LocalDateTime endTime
//) {}
