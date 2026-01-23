package com.spring.jwt.socketio.repository;

import com.spring.jwt.entity.Status;
import com.spring.jwt.socketio.entity.LaptopBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopBidRepository  extends JpaRepository<LaptopBid, Long> {
    List<LaptopBid> findByAuction_AuctionIdOrderByAmountDesc(
            Long auctionId
    );

    // Top 3 bids (highest first)
    List<LaptopBid> findTop3ByAuction_AuctionIdOrderByAmountDescCreatedAtAsc(
            Long auctionId
    );

    // Top (highest) bid
    Optional<LaptopBid> findTopByAuction_AuctionIdOrderByAmountDescCreatedAtAsc(
            Long auctionId
    );

}
