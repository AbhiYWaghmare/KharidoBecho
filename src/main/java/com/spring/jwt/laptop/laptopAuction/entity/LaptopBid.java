<<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/entity/Bid.java
package com.spring.jwt.car.auction.entity;
========
package com.spring.jwt.laptop.laptopAuction.entity;
>>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/laptop/laptopAuction/entity/LaptopBid.java

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
<<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/entity/Bid.java
@Table(name = "car_bids")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Bid {
========
@Table(name = "laptop_bids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopBid {
>>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/laptop/laptopAuction/entity/LaptopBid.java

    public enum BidStatus {
        PLACED,
        WINNING_OFFER,
        ACCEPTED,
        REJECTED,
        EXPIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
<<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/entity/Bid.java
    private Auction auction;
========
    private LaptopAuction auction;
>>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/laptop/laptopAuction/entity/LaptopBid.java

    @Column(nullable = false)
    private Long bidderUserId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
<<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/entity/Bid.java
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime offerExpiresAt;
}
========
    @Column(name = "status", nullable = false)
    private BidStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "offer_expires_at")
    private LocalDateTime offerExpiresAt;


}
>>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/laptop/laptopAuction/entity/LaptopBid.java
