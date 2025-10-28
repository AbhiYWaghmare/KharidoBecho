package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaptopBookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(Status status);

    List<Booking> findByLaptopIdAndStatus(Long laptopId,Status status);
}
