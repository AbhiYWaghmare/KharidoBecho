package com.spring.jwt.auction.repository;

import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.entity.Auction.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // To start auctions whose startTime <= now
    List<Auction> findByStatusAndStartTimeLessThanEqual(Status status, OffsetDateTime now);

    // To end auctions whose endTime < now
    List<Auction> findByStatusAndEndTimeLessThanEqual(Status status, OffsetDateTime now);

    @Query("""
        select a from Auction a
        where a.status = :status
          and a.endTime < :now
        """)
    List<Auction> findExpiredAuctions(Status status, OffsetDateTime now);
}
