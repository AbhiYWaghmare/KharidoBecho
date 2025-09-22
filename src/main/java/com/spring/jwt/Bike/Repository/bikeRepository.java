package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface bikeRepository extends JpaRepository<Bike ,Long> {
    //  Find bikes by sellerId and status
    Page<Bike> findBySellerIdAndStatus(Long sellerId, bikeStatus status, Pageable pageable);

    // Find all bikes by status
    Page<Bike> findByStatus(bikeStatus status, Pageable pageable);

    //  Count bikes by sellerId and status
    Long countBySellerIdAndStatus(Long sellerId, bikeStatus status);


}
