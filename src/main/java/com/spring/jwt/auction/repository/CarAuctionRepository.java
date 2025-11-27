package com.spring.jwt.auction.repository;

import com.spring.jwt.auction.entity.CarAuction;
import com.spring.jwt.auction.entity.CarAuction.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface CarAuctionRepository extends JpaRepository<CarAuction, Long> {
    List<CarAuction> findByStatusAndStartTimeLessThanEqual(Status status, OffsetDateTime now);
    List<CarAuction> findByStatusAndEndTimeLessThanEqual(Status status, OffsetDateTime now);

    @Query("""
        select a from CarAuction a
        where a.status = :status
          and a.endTime < :now
        """)
    List<CarAuction> findExpiredAuctions(Status status, OffsetDateTime now);
}
