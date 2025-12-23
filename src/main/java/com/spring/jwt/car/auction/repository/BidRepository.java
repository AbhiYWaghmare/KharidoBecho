package com.spring.jwt.car.auction.repository;

import com.spring.jwt.car.auction.entity.Bid;
import com.spring.jwt.car.auction.entity.Auction;
import com.spring.jwt.car.auction.entity.Bid.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    // All bids for an auction, ordered by amount descending and createdAt ascending
    @Query("""
        SELECT b FROM Bid b
        WHERE b.auction.auctionId = :auctionId
        ORDER BY b.amount DESC, b.createdAt ASC
    """)
    List<Bid> findAllByAuctionOrderByAmountDesc(@Param("auctionId") Long auctionId);

    // Find bids that have expired for a given auction and status
    @Query("""
        SELECT b FROM Bid b
        WHERE b.auction = :auction
          AND b.status = :status
          AND b.offerExpiresAt < :now
    """)
    List<Bid> findExpiredOffers(@Param("auction") Auction auction,
                                @Param("status") Status status,
                                @Param("now") LocalDateTime now);

    // Optional: top 3 bids for an auction (descending by amount, ascending by createdAt)
    @Query("""
        SELECT b FROM Bid b
        WHERE b.auction.auctionId = :auctionId
        ORDER BY b.amount DESC, b.createdAt ASC
    """)
    List<Bid> findTop3ByAuctionId(@Param("auctionId") Long auctionId);

    // Simple Spring Data method for convenience
    List<Bid> findByAuction_AuctionId(Long auctionId);
}
