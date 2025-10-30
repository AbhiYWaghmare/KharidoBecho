package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LaptopBookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findByStatus(Status status);

    boolean existsByLaptopIdAndStatus(Long laptopId, Status status);

//    List<Booking> findByLaptopIdAndStatus(Long laptopId,Status status);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Laptop l WHERE l.serialNumber = :serialNumber")
    Optional<Laptop> findBySerialNumberForUpdate(@Param("serialNumber") String serialNumber);

}
