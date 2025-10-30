package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.entity.Buyer;
import com.spring.jwt.exception.bookings.BookingNotFoundException;
import com.spring.jwt.exception.bookings.PendingBookingException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.exception.mobile.BuyerNotFoundException;
import com.spring.jwt.laptop.dto.LaptopBookingDTO;
import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopBookingRepository;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopBookingService;
import com.spring.jwt.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LaptopBookingServiceImpl implements LaptopBookingService {

    private final LaptopBookingRepository laptopBookingRepository;
    private final LaptopRepository laptopRepository;
    private final BuyerRepository buyerRepository;



    @Transactional
    @Override
    public Booking createBooking(LaptopBookingDTO laptopBookingDTO) {

        Long buyerId = laptopBookingDTO.getBuyerId();

        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new BuyerNotFoundException(buyerId));


        Laptop laptop = laptopRepository.findBySerialNumber(laptopBookingDTO.getSerialNumber())
                .orElseThrow(() -> new LaptopNotFoundException(
                        "Laptop with serial number " + laptopBookingDTO.getSerialNumber() + " not found"));


        boolean hasActiveBooking = laptopBookingRepository
                .existsByLaptopIdAndStatus(laptop.getId(), Status.ACTIVE);

        Booking newBooking = new Booking();
        newBooking.setBuyer(buyer);
        newBooking.setLaptop(laptop);
        newBooking.setOnDate(laptopBookingDTO.getBookingDate());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(hasActiveBooking ? Status.PENDINGREQUEST : Status.ACTIVE);

        return laptopBookingRepository.save(newBooking);
    }

    @Override
    public List<Booking> getAllPendingBookings() {
        return laptopBookingRepository.findByStatus(Status.PENDINGREQUEST);
    }

    @Override
    public Booking getPendingBookingsByBuyerId(Long buyerId){
        return laptopBookingRepository.findById(buyerId)
                .orElseThrow(() -> new BuyerNotFoundException(buyerId));
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

        if(booking.getStatus() != Status.PENDINGREQUEST){
            throw new PendingBookingException("Only pending booking can rejected/cancel");
        }

        booking.setStatus(Status.ACTIVE);
        return laptopBookingRepository.save(booking);
    }

}
