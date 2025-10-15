package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface LaptopBookingService {

    Booking createBooking(LaptopRequestDTO laptopRequestDTO);

    List<Booking> getPendingBookings();

    Booking approveBooking(Long id);

    Booking rejectBooking(Long id);

    boolean datesOverlap(Booking existing, LocalDate bookingDate);

}
