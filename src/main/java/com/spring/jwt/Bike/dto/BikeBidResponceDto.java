    package com.spring.jwt.Bike.dto;

    import lombok.Data;
    import lombok.RequiredArgsConstructor;

    @Data
    @RequiredArgsConstructor
    public class BikeBidResponceDto {

        private Integer biddingId;
        private Long bikeId;
        private Double highestBid;
        private Integer highestBidderId;
        private String message;
    }
