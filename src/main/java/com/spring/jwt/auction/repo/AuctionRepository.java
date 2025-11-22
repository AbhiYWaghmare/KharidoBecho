package com.spring.jwt.auction.repo;

import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.entity.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByListingIdAndStatus(Long listingId, AuctionStatus status);
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, OffsetDateTime time);
}
