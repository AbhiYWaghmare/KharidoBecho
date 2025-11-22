package com.spring.jwt.auction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Column(name = "bidder_user_id", nullable = false)
    private Long bidderUserId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BidStatus status;

    @Column(name = "offer_expires_at")
    private OffsetDateTime offerExpiresAt;
}
