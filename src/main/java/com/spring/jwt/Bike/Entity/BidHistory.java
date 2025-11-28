package com.spring.jwt.Bike.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bike_bid_history")
@Data
public class BidHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bidding_id")
    private BikeBidding bidding;

    private Integer bidderId;

    private Double amount;

    private LocalDateTime bidTime;
}

