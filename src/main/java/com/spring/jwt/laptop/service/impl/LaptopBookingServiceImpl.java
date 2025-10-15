package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.entity.Laptop;
//import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopBookingRepository;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopBookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LaptopBookingServiceImpl implements LaptopBookingService {

    LaptopBookingRepository laptopBookingRepository;
    LaptopRepository laptopRepository;

    Laptop laptop;


    @Override
    public Booking createBooking(LaptopRequestDTO laptopRequestDTO) {
            String serialNumber = laptopRequestDTO.getSerialNumber();

            boolean exists = laptopRepository.existsBySerialNumber(serialNumber);
            if(!exists){
                throw new LaptopNotFoundException("Laptop not found with serial number " +serialNumber);
            }

            //This will check if laptop is ACTIVE or not
            List<Booking> activeBookings = laptopBookingRepository.findByLaptopIdAndStatus(
                    laptop.getId(),Status.AVAILABLE
            );

            //This will check if booking request is pending or not
             List<Booking> pendingBookings = laptopBookingRepository.findByLaptopIdAndStatus(
                laptop.getId(), Status.PENDING
            );

            List<Booking> existingBookings = Stream.concat(activeBookings.stream(), pendingBookings.stream())
                .collect(Collectors.toList());

            boolean hasOverlap = existingBookings.stream()
                .anyMatch(existing -> datesOverlap(existing, laptopRequestDTO.getBookingDate()));

        Booking newBooking = new Booking();
        newBooking.setLaptop(laptop);
        newBooking.setOnDate(laptopRequestDTO.getBookingDate());
        newBooking.setStatus(Status.PENDING);


        return laptopBookingRepository.save(newBooking);
    }

    @Override
    public List<Booking> getPendingBookings() {
        return null;
    }

    @Override
    public Booking approveBooking(Long id) {
        return null;
    }

    @Override
    public Booking rejectBooking(Long id) {
        return null;
    }

    @Override
    public boolean datesOverlap(Booking existing, LocalDate bookingDate) {
        return false;
    }
}
