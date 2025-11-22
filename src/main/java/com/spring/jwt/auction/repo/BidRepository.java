package com.spring.jwt.auction.repo;

import com.spring.jwt.auction.entity.Bid;
import com.spring.jwt.auction.entity.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionIdOrderByAmountDesc(Long auctionId);
    List<Bid> findTop3ByAuctionIdOrderByAmountDesc(Long auctionId);
    List<Bid> findByStatusAndOfferExpiresAtBefore(BidStatus status, OffsetDateTime time);
}
