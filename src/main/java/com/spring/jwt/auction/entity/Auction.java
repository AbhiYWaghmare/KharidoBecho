package com.spring.jwt.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    public enum Status {
        SCHEDULED,
        RUNNING,
        ENDED,      // bidding time over, waiting for winner to accept
        COMPLETED,  // sold or closed
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

    // Link to your existing Mobile listing
    @Column(name = "mobile_id", nullable = false)
    private Long mobileId;

    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "min_increment_in_rupees", nullable = false)
    private BigDecimal minIncrementInRupees;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // highest current bidder while RUNNING
    @Column(name = "highest_bidder_user_id")
    private Long highestBidderUserId;

    // optimistic locking to avoid race conditions
    @Version
    private Long version;
}
