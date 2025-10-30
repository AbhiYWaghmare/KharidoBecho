package com.spring.jwt.car.services.impl;

import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.repository.CarBookingRepository;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.car.services.CarBookingService;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.exception.car.BookingNotFoundException;
import com.spring.jwt.exception.car.BuyerNotFoundException;
import com.spring.jwt.exception.car.CarNotFoundException;
import com.spring.jwt.exception.car.InvalidBookingOperationException;
import com.spring.jwt.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBookingServiceImpl implements CarBookingService {

    private final CarBookingRepository carBookingRepository;
    private final CarRepository carRepository;
    private final BuyerRepository buyerRepository;

    @Transactional
    @Override
    public CarBooking createBooking(CarBookingDTO dto) {

        Buyer buyer = buyerRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id " + dto.getBuyerId()));


        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + dto.getCarId() + " not found"));

        boolean hasActiveBooking = carBookingRepository
                .existsByCar_CarIdAndBookingStatus(car.getCarId(), CarBooking.Status.ACTIVE);

        CarBooking booking = CarBooking.builder()
                .buyer(buyer)
                .car(car)
                .bookingDate(OffsetDateTime.now())

                .bookingStatus(hasActiveBooking ? CarBooking.Status.PENDING : CarBooking.Status.ACTIVE)
                .build();

        return carBookingRepository.save(booking);
    }

    @Override
    public List<CarBooking> getPendingBookings() {
        return carBookingRepository.findByBookingStatus(CarBooking.Status.PENDING);
    }

    @Override
    public CarBooking approveBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        booking.setBookingStatus(CarBooking.Status.SOLD);

        // mark car as sold
        Car car = booking.getCar();
        car.setStatus(Car.Status.SOLD);
        carRepository.save(car);

        return carBookingRepository.save(booking);
    }

    @Override
    public CarBooking rejectBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (booking.getBookingStatus() != CarBooking.Status.PENDING) {
            throw new InvalidBookingOperationException("Only pending bookings can be rejected");
        }

        booking.setBookingStatus(CarBooking.Status.CANCELLED);
        return carBookingRepository.save(booking);
    }
}
