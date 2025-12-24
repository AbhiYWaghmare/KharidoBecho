package com.spring.jwt.car.auction.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

=======
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AuctionDTO(
<<<<<<< HEAD

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

=======
        Long auctionId,
        Long carId,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal minIncrementInRupees,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d
        Long highestBidderUserId
) {}
