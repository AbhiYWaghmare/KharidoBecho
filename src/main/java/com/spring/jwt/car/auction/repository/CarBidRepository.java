//package com.spring.jwt.car.auction.repository;
//
//import com.spring.jwt.car.auction.entity.CarBid;
//import com.spring.jwt.car.auction.entity.CarAuction;
//import com.spring.jwt.car.auction.entity.CarBid.Status;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
////
////public interface CarBidRepository extends JpaRepository<CarBid, Long> {
////
////    // All bids for an auction, ordered by amount descending and createdAt ascending
////    @Query("""
////        SELECT b FROM Bid b
////        WHERE b.auction.auctionId = :auctionId
////        ORDER BY b.amount DESC, b.createdAt ASC
////    """)
////    List<CarBid> findAllByAuctionOrderByAmountDesc(@Param("auctionId") Long auctionId);
////
////    // Find bids that have expired for a given auction and status
////    @Query("""
////        SELECT b FROM Bid b
////        WHERE b.auction = :auction
////          AND b.status = :status
////          AND b.offerExpiresAt < :now
////    """)
////    List<CarBid> findExpiredOffers(@Param("auction") CarAuction auction,
////                                   @Param("status") Status status,
////                                   @Param("now") LocalDateTime now);
////
////    // Optional: top 3 bids for an auction (descending by amount, ascending by createdAt)
//////    @Query("""
//////        SELECT b FROM Bid b
//////        WHERE b.auction.auctionId = :auctionId
//////        ORDER BY b.amount DESC, b.createdAt ASC
//////    """)
//////    List<CarBid> findTop3ByAuctionId(@Param("auctionId") Long auctionId);
////    @Query("""
////    SELECT b FROM CarBid b
////    WHERE b.auction.auctionId = :auctionId
////    ORDER BY b.amount DESC, b.createdAt ASC
////""")
////    List<CarBid> findTop3ByAuctionId(@Param("auctionId") Long auctionId);
////
////    // Simple Spring Data method for convenience
////    List<CarBid> findByAuction_AuctionId(Long auctionId);
////}
//public interface CarBidRepository extends JpaRepository<CarBid, Long> {
//
//    // ✅ All bids for an auction
//    @Query("""
//        SELECT b FROM CarBid b
//        WHERE b.auction.auctionId = :auctionId
//        ORDER BY b.amount DESC, b.createdAt ASC
//    """)
//    List<CarBid> findAllByAuctionOrderByAmountDesc(@Param("auctionId") Long auctionId);
//
//    // ✅ Expired offers
//    @Query("""
//        SELECT b FROM CarBid b
//        WHERE b.auction = :auction
//          AND b.status = :status
//          AND b.offerExpiresAt < :now
//    """)
//    List<CarBid> findExpiredOffers(@Param("auction") CarAuction auction,
//                                   @Param("status") Status status,
//                                   @Param("now") LocalDateTime now);
//
//    // ⚠️ JPQL मध्ये Top3 नसतं → service मध्ये limit लाव
//    @Query("""
//        SELECT b FROM CarBid b
//        WHERE b.auction.auctionId = :auctionId
//        ORDER BY b.amount DESC, b.createdAt ASC
//    """)
//    List<CarBid> findTop3ByAuctionId(@Param("auctionId") Long auctionId);
//
//    // ✅ Derived query (safe)
//    List<CarBid> findByAuction_AuctionId(Long auctionId);
//}
//
