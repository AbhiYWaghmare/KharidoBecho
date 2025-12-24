package com.spring.jwt.car.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
<<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/dto/BidDTO.java
========
import java.time.OffsetDateTime;
>>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/auction/dto/BidDTO.java

public record BidDTO(
        Long bidId,
        Long auctionId,
        Long bidderUserId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {}
