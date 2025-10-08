package com.spring.jwt.car.repository;

import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByRegistration(String registration);

    List<Car> findByRegistrationIsNull();

    Page<Car> findByCarStatus(Status status, Pageable pageable);

    @Query("SELECT c FROM Car c WHERE c.seller.sellerId = :sellerId AND c.carStatus = :status")
    Page<Car> findBySellerIdAndCarStatus(
            @Param("sellerId") Long sellerId,
            @Param("status") Status status,
            Pageable pageable
    );

    @Query("SELECT COUNT(c) FROM Car c WHERE c.seller.sellerId = :sellerId AND c.carStatus = :status")
    long countBySellerIdAndCarStatus(@Param("sellerId") Long sellerId,
                                     @Param("status") Status status);
}
