package com.spring.jwt.laptop.laptopAuction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "laptop_auction")
public class LaptopAuction {
    public enum AuctionStatus {
        SCHEDULED,
        RUNNING,
        ENDED,

        COMPLETED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

    @Column(name = "laptop_id", nullable = false)
    private Long laptopId;

    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "min_increment_in_rupees", nullable = false)
    private BigDecimal minIncrementInRupees;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuctionStatus status;

    @Column(name = "highest_bidder_user_id")
    private Long highestBidderUserId;

    @Version
    private Long version;
}
