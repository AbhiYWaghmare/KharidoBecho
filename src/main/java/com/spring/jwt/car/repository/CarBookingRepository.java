//package com.spring.jwt.car.repository;
//
//import com.spring.jwt.car.entity.CarBooking;
//import com.spring.jwt.entity.Status;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface CarBookingRepository extends JpaRepository<CarBooking, Long> {
//    List<CarBooking> findByStatus(Status status);
//    List<CarBooking> findByCarIdAndStatus(Long carId, Status status);
//    List<CarBooking> findByCarCarIdAndStatus(Long carId, Status status);
//}

package com.spring.jwt.car.repository;

import com.spring.jwt.car.entity.CarBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarBookingRepository extends JpaRepository<CarBooking, Long> {

    // Get all bookings by status
//    List<CarBooking> findByStatus(Status status);

    // Find bookings by Car ID and Status
//    List<CarBooking> findByCarCarIdAndStatus(Long carId, Status status);
    List<CarBooking> findByBookingStatus(CarBooking.Status bookingStatus);
    List<CarBooking> findByCar_CarId(Long carId);
    boolean existsByCar_CarIdAndBookingStatus(Long carId, CarBooking.Status bookingStatus);
    boolean existsByBuyer_BuyerIdAndCar_CarId(Long buyerId, Long carId);
    List<CarBooking> findByBuyer_BuyerId(Long buyerId);

    List<CarBooking> findBySeller_SellerId(Long sellerId);
    @Query("SELECT c FROM CarBooking c WHERE c.seller.sellerId = :sellerId")
    List<CarBooking> findBySellerId(@Param("sellerId") Long sellerId);



//    List<CarBooking> findBySellerId(Long sellerId);


}

