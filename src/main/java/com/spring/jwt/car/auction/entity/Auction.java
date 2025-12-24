package com.spring.jwt.car.auction.entity;

import com.spring.jwt.car.entity.Car;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_auctions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Auction {

    public enum Status {
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(nullable = false)
    private BigDecimal startPrice;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private BigDecimal minIncrementInRupees;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "highest_bidder_user_id")
    private Long highestBidderUserId;

    @Version
    private Long version;
}
