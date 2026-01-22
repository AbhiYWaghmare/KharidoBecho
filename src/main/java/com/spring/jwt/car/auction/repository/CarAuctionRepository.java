//package com.spring.jwt.car.auction.repository;
//
//import com.spring.jwt.car.auction.entity.CarAuction;
//import com.spring.jwt.car.auction.entity.CarAuction.Status;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface CarAuctionRepository extends JpaRepository<CarAuction, Long> {
//
//    // Find auctions that are SCHEDULED and their start time has passed (ready to start)
//    List<CarAuction> findByStatusAndStartTimeLessThanEqual(Status status, LocalDateTime now);
//
//    // Find auctions that are RUNNING and their end time has passed (ready to end)
//    List<CarAuction> findByStatusAndEndTimeLessThanEqual(Status status, LocalDateTime now);
//
//    // Optional custom query if you want explicitly for expired auctions in processExpiredOffers
//    @Query("SELECT c FROM CarAuction c WHERE c.status = :status AND c.endTime <= :now")
//    List<CarAuction> findExpiredAuctions(@Param("status") CarAuction.Status status, @Param("now") LocalDateTime now);
//
//}
