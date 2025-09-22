//package com.spring.jwt.car.repository;
//
//import com.spring.jwt.entity.Car;
//import com.spring.jwt.entity.Status;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface CarRepository extends JpaRepository<Car, Integer> {
//
//    Optional<Car> findByMainCarId(String mainCarId);
//
//    Page<Car> findByDealerIdAndCarStatus(Integer dealerId, Status carStatus, Pageable pageable);
//
//    Page<Car> findByCarStatus(Status carStatus, Pageable pageable);
//
//    long countByDealerIdAndCarStatus(Integer dealerId, Status carStatus);
//}
//new
package com.spring.jwt.car.repository;

import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findByMainCarId(String mainCarId);
    Page<Car> findByDealerIdAndCarStatus(Integer dealerId, Status status, Pageable pageable);
    Page<Car> findByCarStatus(Status status, Pageable pageable);
    long countByDealerIdAndCarStatus(Integer dealerId, Status status);
}

