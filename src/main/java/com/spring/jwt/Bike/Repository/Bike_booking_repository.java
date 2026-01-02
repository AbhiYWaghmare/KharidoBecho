package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.Bike_booking;
import com.spring.jwt.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface Bike_booking_repository extends JpaRepository<Bike_booking, Long> {
    List<Bike_booking> findByStatus(Bike_booking.BookingStatus status);
    List<Bike_booking> findByBikeAndBuyer(Bike bike, Buyer buyer);
    // For Buyer
    List<Bike_booking> findByBuyer_BuyerId(Long buyerId);

    // For Seller
    //List<Bike_booking> findByBike_Seller_Seller_Id(Long sellerUserId);
    List<Bike_booking> findByBike_Seller_SellerId(Long sellerId);
//    List<Bike_booking> findByBike_BikeId(Long bikeId);
//    List<Bike_booking> findByBike_Bike_id(Long bikeId);
    @Query("SELECT bb FROM Bike_booking bb WHERE bb.bike.bike_id = :bikeId")
    List<Bike_booking> findByBikeId(@Param("bikeId") Long bikeId);

}
