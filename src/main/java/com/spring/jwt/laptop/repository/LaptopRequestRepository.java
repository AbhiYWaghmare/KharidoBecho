package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.entity.LaptopBooking;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LaptopRequestRepository extends JpaRepository<LaptopBooking, Long> {
//    List<LaptopBooking> findByLaptop_Id(Long laptopId);

    List<LaptopBooking> findByLaptop_IdOrderByCreatedAtAsc(Long laptopId);

    List<LaptopBooking> findByBuyer_BuyerId(Long buyerId);

    boolean existsByLaptop_IdAndPendingStatus(Long laptopId, LaptopRequestStatus pendingStatus);

    List<LaptopBooking> findByLaptop_IdAndPendingStatus(Long laptopId, LaptopRequestStatus pendingStatus);

//    List<LaptopBooking> findBySellerSellerId(Long sellerId);
//
//    List<LaptopBooking> findByLaptopBookingId(Long bookingId);

    @Query("""
   SELECT lb FROM LaptopBooking lb
   JOIN FETCH lb.buyer b
   JOIN FETCH lb.laptop l
   JOIN FETCH l.seller s
   WHERE lb.laptopBookingId = :id
""")
    Optional<LaptopBooking> findByIdWithDetails(@Param("id") Long id);

    @Query("""
   SELECT lb FROM LaptopBooking lb
   JOIN FETCH lb.buyer b
   JOIN FETCH lb.seller s
   JOIN FETCH lb.laptop l
   WHERE b.buyerId = :buyerId
""")
    List<LaptopBooking> findByBuyerWithDetails(@Param("buyerId") Long buyerId);


    @Query("""
   SELECT lb FROM LaptopBooking lb
   JOIN FETCH lb.buyer b
   JOIN FETCH lb.seller s
   JOIN FETCH lb.laptop l
   WHERE s.sellerId = :sellerId
""")
    List<LaptopBooking> findBySellerWithDetails(Long sellerId);

    @Query("""
   SELECT lb FROM LaptopBooking lb
   JOIN FETCH lb.buyer b
   JOIN FETCH lb.seller s
   JOIN FETCH lb.laptop l
   WHERE l.id = :laptopId
   ORDER BY lb.createdAt ASC
""")
    List<LaptopBooking> findByLaptopWithDetails(@Param("laptopId") Long laptopId);

}