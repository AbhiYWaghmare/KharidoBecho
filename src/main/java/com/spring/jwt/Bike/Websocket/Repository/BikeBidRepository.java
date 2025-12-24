package com.spring.jwt.Bike.Websocket.Repository;

import com.spring.jwt.Bike.Websocket.Entity.BikeAuction;
import com.spring.jwt.Bike.Websocket.Entity.BikeBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BikeBidRepository extends JpaRepository<BikeBid, Long> {

    @Query("""
        select b from BikeBid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
    """)
    List<BikeBid> findAllByAuctionOrderByAmountDesc(Long auctionId);

    @Query("""
        select b from BikeBid b
        where b.auction = :auction
          and b.status = :status
          and b.offerExpiresAt < :now
    """)
    List<BikeBid> findExpiredOffers(BikeAuction auction,
                                    BikeBid.BidStatus status,
                                    LocalDateTime now);
}

