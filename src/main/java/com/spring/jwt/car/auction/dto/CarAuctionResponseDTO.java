package com.spring.jwt.car.auction.dto;

import com.spring.jwt.car.entity.Car;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CarAuctionResponseDTO {

    private Long auctionId;
    private Car car;

    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private BigDecimal minIncrementInRupees;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;
    private Long highestBidderUserId;
    private Long version;
}
