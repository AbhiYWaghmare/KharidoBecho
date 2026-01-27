package com.spring.jwt.socketio.repository;


import com.spring.jwt.socketio.entity.LaptopAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LaptopAuctionRepository extends JpaRepository<LaptopAuction, Long> {


    List<LaptopAuction> findByStatus(LaptopAuction.AuctionStatus status);


    boolean existsByLaptop_IdAndStatus(
            Long laptopId,
            LaptopAuction.AuctionStatus status
    );

    List<LaptopAuction> findByStatusAndStartTimeLessThanEqual(
            LaptopAuction.AuctionStatus status,
            LocalDateTime now
    );

    List<LaptopAuction> findByStatusAndEndTimeLessThanEqual(
            LaptopAuction.AuctionStatus status,
            LocalDateTime now
    );

}
