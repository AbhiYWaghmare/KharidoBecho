package com.spring.jwt.laptop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bid_laptops")
@Data
@RequiredArgsConstructor
public class BidLaptops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beading_id")
    private Long beadingId;

    // Many bids belong to one auction
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beading_laptop_id", nullable = false)
    private BeadingLaptop beadingLaptop;

    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;

    @Column(name = "bid_amount", nullable = false)
    private Double bidAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "closing_at", nullable = false)
    private LocalDateTime closingAt;
}
