package com.spring.jwt.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "car_bids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarBid {

    public enum Status {
        PLACED,
        WINNING_OFFER,
        ACCEPTED,
        REJECTED,
        EXPIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private CarAuction auction;

    @Column(name = "bidder_user_id", nullable = false)
    private Long bidderUserId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "offer_expires_at")
    private OffsetDateTime offerExpiresAt;
}
