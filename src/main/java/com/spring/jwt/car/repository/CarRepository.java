package com.spring.jwt.car.repository;

import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.entity.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByTitleAndSeller_SellerId(String title, Long sellerId);

    Optional<Car> findByCarIdAndDeletedFalse(Long carId);

    Page<Car> findByDeletedFalse(Pageable pageable);

    Page<Car> findBySeller_SellerIdAndDeletedFalse(Long sellerId, Pageable pageable);

    Optional<Car> findByRegistrationNumber(String registrationNumber);
    List<CarBooking> findBySeller_SellerId(Long sellerId);

    List<CarBooking> findByBuyer(Buyer buyer);



}
