package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
     boolean existsBySerialNumber(String serialNumber);

     Page<Laptop> findBySellerIdAndStatus(Long sellerId, Status status, Pageable pageable);

     Page<Laptop> findByStatus(Status status, Pageable pageable);


//     @Query("SELECT COUNT(l) FROM Laptop l " +
//             "WHERE l.seller_id = :dealerId " +
//             "AND (:status IS NULL OR l.status = :status)")
//     long countByDealerIdAndStatus(@Param("seller_id") Long dealerId, @Param("status") String status);
}
