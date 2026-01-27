package com.spring.jwt.auction.repository;

import com.spring.jwt.auction.entity.Bid;
import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.entity.Bid.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("""
        select b from Bid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
        """)
    List<Bid> findAllByAuctionOrderByAmountDesc(Long auctionId);

    @Query("""
        select b from Bid b
        where b.auction = :auction
          and b.status = :status
          and b.offerExpiresAt < :now
        """)
    List<Bid> findExpiredOffers(Auction auction, Status status, LocalDateTime now);

    @Query("""
        select b from Bid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
        """)
    List<Bid> findTop3ByAuctionOrderByAmountDesc(Long auctionId);
}
