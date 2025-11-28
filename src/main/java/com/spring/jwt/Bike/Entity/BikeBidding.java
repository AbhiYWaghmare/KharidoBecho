package com.spring.jwt.Bike.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bike_bidding")
@Data
public class BikeBidding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidding_id")
    private Integer biddingId;

    //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bike_id")
    private Bike bike;

    @Column(name = "highest_bid")
    private Double highestBid = 0.0;

    @Column(name = "highest_bidder_id")
    private Integer highestBidderId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // allow updates to closingAt
    @Column(name = "closing_at", nullable = false)
    private LocalDateTime closingAt;

    // flag used by scheduler and checks
    private boolean active = true;

}
