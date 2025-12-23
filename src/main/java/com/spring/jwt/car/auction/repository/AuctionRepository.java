package com.spring.jwt.car.auction.repository;

import com.spring.jwt.car.auction.entity.Auction;
import com.spring.jwt.car.auction.entity.Auction.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // Find auctions that are SCHEDULED and their start time has passed (ready to start)
    List<Auction> findByStatusAndStartTimeLessThanEqual(Status status, LocalDateTime now);

    // Find auctions that are RUNNING and their end time has passed (ready to end)
    List<Auction> findByStatusAndEndTimeLessThanEqual(Status status, LocalDateTime now);

    // Optional custom query if you want explicitly for expired auctions in processExpiredOffers
    @Query("""
        SELECT a FROM Auction a
        WHERE a.status = :status
          AND a.endTime <= :now
    """)
    List<Auction> findExpiredAuctions(@Param("status") Status status, @Param("now") LocalDateTime now);
}
