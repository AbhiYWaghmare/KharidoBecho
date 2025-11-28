package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Integer> {

    List<BidHistory> findByBidding_BiddingId(Integer biddingId);


}
