package com.spring.jwt.car.services;

import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.CarBooking;

import java.util.List;

public interface CarBookingService {
    CarBooking createBooking(CarBookingDTO carBookingDTO);
    CarBooking acceptBooking(Long bookingId);
    List<CarBooking> getPendingBookings();
    CarBooking approveBooking(Long bookingId);
    CarBooking rejectBooking(Long bookingId);
    CarBooking addMessage(Long id, CarBookingDTO conversation);
    List<CarBooking> getBookingsByBuyerId(Long buyerId);

    List<CarBooking> getBookingsBySellerId(Long sellerId);


    CarBooking getBookingDetails(Long bookingId);


}
