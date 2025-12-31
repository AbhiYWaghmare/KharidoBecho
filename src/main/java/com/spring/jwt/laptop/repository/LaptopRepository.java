package com.spring.jwt.laptop.repository;

import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    boolean existsBySerialNumber(String serialNumber);

    @Query("SELECT l FROM Laptop l WHERE l.seller.sellerId = :sellerId AND l.status = :status")
    Page<Laptop> findBySellerIdAndStatus(@Param("sellerId") Long sellerId,
                                       @Param("status") Status status,
                                       Pageable pageable);

    Page<Laptop> findByStatus(Status pendingStatus, Pageable pageable);


    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.seller.sellerId = :sellerId AND l.status = :status")
    Long countBySellerAndStatus(@Param("sellerId") Long sellerId,
                                @Param("status") Status status);


    Optional<Laptop> findByIdAndDeletedFalse(Long id);

//    @EntityGraph(attributePaths = "laptopPhotos")
    Page<Laptop> findBySeller_SellerId(Long sellerId, Pageable pageable);

    @Query("""
    SELECT l
    FROM Laptop l
    LEFT JOIN FETCH l.laptopPhotos
    WHERE l.id = :laptopId
""")
    Laptop findByIdWithPhotos(@Param("laptopId") Long laptopId);

}
