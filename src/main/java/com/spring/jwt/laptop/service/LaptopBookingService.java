package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.dto.LaptopBookingDTO;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Booking;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@Service
public interface LaptopBookingService {

    Booking createBooking(LaptopBookingDTO laptopBookingDTO);

    List<Booking> getAllPendingBookings();

    Booking getPendingBookingsByBuyerId(Long buyerId);


    Booking approveBooking(Long id);

    Booking rejectBooking(Long id);


}
