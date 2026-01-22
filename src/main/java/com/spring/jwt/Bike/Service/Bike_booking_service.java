package com.spring.jwt.Bike.Service;


import com.spring.jwt.Bike.Entity.Bike_booking;
import com.spring.jwt.Bike.dto.Bike_booking_dto;
import com.spring.jwt.entity.Buyer;

import java.util.List;

public interface Bike_booking_service {
    Bike_booking createBooking(Bike_booking_dto bikeBookingDto );

   List<Bike_booking> getBookings();

  Bike_booking completeBooking(Long id);

  Bike_booking rejectBooking(Long id);
  Bike_booking deleteBooking(Long id);
  Bike_booking addMessage(Long id, Bike_booking_dto conversation);
  Bike_booking getBookingById(Long id);
  Bike_booking updateBookingStatus(Long bookingId, Bike_booking.BookingStatus newStatus);
    List<Bike_booking> getBookingsByBuyerId(Long buyerId);

    List<Bike_booking> getBookingsBySellerId(Long sellerId);
    List<Bike_booking> getBookingsByBikeId(Long bikeId);




}

