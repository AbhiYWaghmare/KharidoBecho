package com.spring.jwt.laptop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.jwt.laptop.model.AuctionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "beading_laptops")
@Entity
public class BeadingLaptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beadingLaptopId;

    @Column(unique = true)
    private String uniqueBeadingLaptopId;

    @Column(name = "laptop_id")
    private Long laptopId; // FK - existing laptop

    @Column(name = "base_price")
    private Double basePrice;
    @Column(name ="min_increment")
    private Integer minIncrement;

    @Column(name = "highest_bid")
    private Double highestBid;
    @Column(name = "highest_bidder_id")
    private Long highestBidderId;

    @Column(name = "seller_id")
    private Long sellerId;
    private Long dealerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "closing_time")
    private LocalDateTime closingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus auctionStatus;   // LIVE / CLOSED

    @Column(name = "bidding_timer_id")
    private Integer biddingTimerId;

    @Column(name = "bidding_timer_status")
    private String biddingTimerStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "beadingLaptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BidLaptops> bids = new ArrayList<>();

}
