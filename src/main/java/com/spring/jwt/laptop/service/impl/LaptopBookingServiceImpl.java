package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.exception.bookings.BookingNotFoundException;
import com.spring.jwt.exception.bookings.PendingBookingException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.laptop.dto.LaptopBookingDTO;
import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopBookingRepository;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LaptopBookingServiceImpl implements LaptopBookingService {

    private final LaptopBookingRepository laptopBookingRepository;
    private final LaptopRepository laptopRepository;



    @Override
    public Booking createBooking(LaptopBookingDTO laptopBookingDTO) {
        String serialNumber = laptopBookingDTO.getSerialNumber();

        Laptop laptop = laptopRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new LaptopNotFoundException(
                        "Laptop with serial number " + serialNumber + " not found"));

        List<Booking> activeBookings = laptopBookingRepository.findByLaptopIdAndStatus(laptop.getId(), Status.ACTIVE);
        List<Booking> pendingBookings = laptopBookingRepository.findByLaptopIdAndStatus(laptop.getId(), Status.PENDING);

        List<Booking> existingBookings = Stream.concat(activeBookings.stream(), pendingBookings.stream())
                .collect(Collectors.toList());


        boolean hasExistingBooking = existingBookings.stream()
                .anyMatch(existing -> existing.getLaptop().getSerialNumber()
                        .equalsIgnoreCase(laptopBookingDTO.getSerialNumber()));

        Booking newBooking = new Booking();
        newBooking.setLaptop(laptop);
        newBooking.setOnDate(laptopBookingDTO.getBookingDate());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(hasExistingBooking ? Status.PENDING : Status.ACTIVE);

        return laptopBookingRepository.save(newBooking);
    }

    @Override
    public List<Booking> getPendingBookings() {
        return laptopBookingRepository.findByStatus(Status.PENDING);
    }

    @Override
    public Booking approveBooking(Long bookingId) {
        Booking booking = laptopBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with booking id " +bookingId));

        booking.setStatus(Status.SOLD);

        return laptopBookingRepository.save(booking);
    }

    @Override
    public Booking rejectBooking(Long bookingId) {
        Booking booking = laptopBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with booking id " +bookingId));

        if(booking.getStatus() != Status.PENDING){
            throw new PendingBookingException("Only pending booking can rejected/cancel");
        }

        booking.setStatus(Status.ACTIVE);
        return laptopBookingRepository.save(booking);
    }

}
