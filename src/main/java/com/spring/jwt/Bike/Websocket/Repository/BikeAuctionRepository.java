//package com.spring.jwt.Bike.Websocket.Repository;
//
//import com.spring.jwt.Bike.Websocket.Entity.BikeAuction;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface BikeAuctionRepository extends JpaRepository<BikeAuction, Long> {
//
//    List<BikeAuction> findByStatusAndStartTimeLessThanEqual(
//            BikeAuction.AuctionStatus status, LocalDateTime now);
//
//    List<BikeAuction> findByStatusAndEndTimeLessThanEqual(
//            BikeAuction.AuctionStatus status, LocalDateTime now);
//
//    @Query("""
//        select a from BikeAuction a
//        where a.status = :status and a.endTime < :now
//    """)
//    List<BikeAuction> findExpiredAuctions(BikeAuction.AuctionStatus status, LocalDateTime now);
//}
//
