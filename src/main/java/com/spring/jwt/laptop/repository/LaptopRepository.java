package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    boolean existsBySerialNumber(String serialNumber);

    Laptop findByMainLaptopId(Long mainLaptopId);

    Page<Laptop> findBySeller_IdAndStatus(Long sellerId, Status status, Pageable pageable);


    Page<Laptop> findByStatus(Status status, Pageable pageable);

    Long countBySellerIdAndStatus(Long sellerId, Status status);


}
