package com.spring.jwt.laptop.laptopAuction.repository;

import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LaptopBidRepository extends JpaRepository<LaptopBid,Long> {
    @Query("""
        select b from LaptopBid b
        where b.auction.auctionId = :auctionId
        order by b.amount desc, b.createdAt asc
    """)
    List<LaptopBid> findAllByAuctionOrderByAmountDesc(Long auctionId);

    @Query("""
        select b from LaptopBid b
        where b.auction = :auction
          and b.status = :status
          and b.offerExpiresAt < :now
    """)
    List<LaptopBid> findExpiredOffers(LaptopAuction auction, LaptopBid.BidStatus status, LocalDateTime now);
}
