package com.spring.jwt.auction.repository;

import com.spring.jwt.auction.entity.CarAuction;
import com.spring.jwt.auction.entity.CarBid.Status;
import com.spring.jwt.auction.entity.CarBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface CarBidRepository extends JpaRepository<CarBid, Long> {

    @Query("""
        select b from CarBid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
        """)
    List<CarBid> findAllByAuctionOrderByAmountDesc(Long auctionId);

    @Query("""
        select b from CarBid b
        where b.auction = :auction
          and b.status = :status
          and b.offerExpiresAt < :now
        """)
    List<CarBid> findExpiredOffers(CarAuction auction, Status status, OffsetDateTime now);

    @Query("""
        select b from CarBid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
        """)
    List<CarBid> findTop3ByAuctionOrderByAmountDesc(Long auctionId);
}
