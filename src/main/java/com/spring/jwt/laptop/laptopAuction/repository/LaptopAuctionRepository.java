package com.spring.jwt.laptop.laptopAuction.repository;

import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LaptopAuctionRepository extends JpaRepository<LaptopAuction,Long> {
    List<LaptopAuction> findByStatusAndStartTimeLessThanEqual(LaptopAuction.AuctionStatus status, LocalDateTime now);

    List<LaptopAuction> findByStatusAndEndTimeLessThanEqual(LaptopAuction.AuctionStatus status, LocalDateTime now);

    @Query("""
        select a from LaptopAuction a
        where a.status = :status
          and a.endTime < :now
    """)
    List<LaptopAuction> findExpiredAuctions(LaptopAuction.AuctionStatus status, LocalDateTime now);
}
