//package com.spring.jwt.car.auction.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "car_bids")
//@Getter @Setter
//@NoArgsConstructor @AllArgsConstructor @Builder
//public class CarBid {
//
//    public enum Status {
//        PLACED,
//        WINNING_OFFER,
//        ACCEPTED,
//        REJECTED,
//        EXPIRED
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long bidId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "auction_id", nullable = false)
//    private CarAuction auction;
//
//    @Column(nullable = false)
//    private Long bidderUserId;
//
//    @Column(nullable = false)
//    private BigDecimal amount;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Status status;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column
//    private LocalDateTime offerExpiresAt;
//}
