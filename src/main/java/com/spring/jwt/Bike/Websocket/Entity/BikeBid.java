package com.spring.jwt.Bike.Websocket.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bike_bids")
public class BikeBid {

    public enum BidStatus {
        PLACED,
        WINNING_OFFER,
        ACCEPTED,
        REJECTED,
        EXPIRED,
        LOST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private BikeAuction auction;

    @Column(name = "bidder_user_id", nullable = false)
    private Long bidderUserId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "offer_expires_at")
    private LocalDateTime offerExpiresAt;
}
