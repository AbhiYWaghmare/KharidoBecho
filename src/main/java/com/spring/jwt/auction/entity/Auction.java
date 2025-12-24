package com.spring.jwt.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "car_auctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarAuction {

    public enum Status {
        SCHEDULED,
        RUNNING,
        ENDED,
        COMPLETED,
        CANCELLED,
        ACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

    @Column(name = "car_id", nullable = false)
    private Long carId;

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

    @Column(name = "highest_bidder_user_id")
    private Long highestBidderUserId;

    @Version
    private Long version;
}
