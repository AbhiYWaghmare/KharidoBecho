//package com.spring.jwt.laptop.laptopAuction.dto;
//
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.FutureOrPresent;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//
//public record LaptopAuctionDTO(
//
//        Long auctionId,
//
//        @NotNull(message = "Laptop ID is required")
//        Long laptopId,
//        @NotNull(message = "Start price is required")
//        @DecimalMin(value = "1.0", message = "Start price must be greater than 0")
//        BigDecimal startPrice,
//
//        @NotNull(message = "Current price is required")
//        @DecimalMin(value = "1.0", message = "Current price must be greater than 0")
//        BigDecimal currentPrice,
//        @NotNull(message = "Minimum increment is required")
//        @DecimalMin(value = "1.0", message = "Minimum increment must be greater than 0")
//        BigDecimal minIncrementInRupees,
////        String status,
//        @NotNull(message = "Start time is required")
////        @FutureOrPresent(message = "Start time cannot be in the past")
//        LocalDateTime startTime,
//        @NotNull(message = "End time is required")
////        @FutureOrPresent(message = "End time cannot be in the past")
//        LocalDateTime endTime,
//        Long highestBidderUserId
//) {}
