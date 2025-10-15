package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface bikeRepository extends JpaRepository<Bike ,Long> {

    // Find all bikes by status
    Page<Bike> findByStatus(bikeStatus status, Pageable pageable);

    Long countBySeller_SellerIdAndStatus(Long sellerId, bikeStatus status);

    Page<Bike> findBySeller_SellerIdAndStatus(Long sellerId, bikeStatus status, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Bike b WHERE b.registrationNumber = :registrationNumber AND b.bike_id <> :bikeId")
    boolean existsByRegistrationNumber(String registrationNumber);
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bike b WHERE b.registrationNumber = :registrationNumber AND b.bike_id <> :bikeId")
    boolean existsByRegistrationNumberAndBike_idNot(@Param("registrationNumber") String registrationNumber, @Param("bikeId") Long bikeId);





}




