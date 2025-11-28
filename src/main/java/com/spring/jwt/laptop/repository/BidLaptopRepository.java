package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.BidLaptops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BidLaptopRepository extends JpaRepository<BidLaptops,Long> {

    List<BidLaptops> findByClosingAtAfter(LocalDateTime now);

}
