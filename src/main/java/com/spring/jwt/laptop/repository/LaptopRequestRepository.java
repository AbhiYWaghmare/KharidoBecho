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
    List<LaptopBooking> findByLaptop_Id(Long laptopId);

    List<LaptopBooking> findByLaptop_IdOrderByCreatedAtAsc(Long laptopId);

    List<LaptopBooking> findByBuyer_BuyerId(Long buyerId);

    boolean existsByLaptop_IdAndPendingStatus(Long laptopId, LaptopRequestStatus pendingStatus);

    List<LaptopBooking> findByLaptop_IdAndPendingStatus(Long laptopId, LaptopRequestStatus pendingStatus);

    List<LaptopBooking> findBySellerSellerId(Long sellerId);


}
