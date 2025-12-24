package com.spring.jwt.Bike.Websocket.Entity;

import com.spring.jwt.Bike.Entity.Bike;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bike_auction")
public class BikeAuction {

    public enum AuctionStatus {
        SCHEDULED,
        ACTIVE,
        ENDED,
        COMPLETED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

//    @Column(name = "bike_id", nullable = false)
//    private Long bikeId;
   @ManyToOne(fetch = FetchType.EAGER, optional = false)
   @JoinColumn(name = "bike_id")
   private Bike bike;


    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "min_increment", nullable = false)
    private BigDecimal minIncrement;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus status;

    @Column(name = "highest_bidder_user_id")
    private Long highestBidderUserId;

    @Version
    private Long version;
}

