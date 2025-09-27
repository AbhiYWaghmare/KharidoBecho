package com.spring.jwt.car.repository;

import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Page<Car> findByDealerIdAndCarStatus(Integer dealerId, Status status, Pageable pageable);

    Page<Car> findByCarStatus(Status status, Pageable pageable);

    long countByDealerIdAndCarStatus(Integer dealerId, Status status);
}
