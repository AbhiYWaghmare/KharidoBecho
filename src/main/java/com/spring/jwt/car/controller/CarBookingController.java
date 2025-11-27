package com.spring.jwt.car.controller;

import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.dto.CarBookingResponseDTO;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.services.CarBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carBookings")
@RequiredArgsConstructor
public class CarBookingController {

    private final CarBookingService carBookingService;

    // ✅ Create booking

    @PostMapping("/createBooking")
    public ResponseEntity<CarBookingResponseDTO> createBooking( @Valid @RequestBody CarBookingDTO carBookingDTO) {
        CarBooking booking = carBookingService.createBooking(carBookingDTO);

        String message = booking.getBookingStatus() == CarBooking.Status.PENDING
                ? "Car booking created successfully (status: pending)"
                : "Car booked successfully!";

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CarBookingResponseDTO(
                        message,
                        booking.getCar() != null ? booking.getCar().getCarId() : null,
                        booking.getBookingId(),
                        booking.getBookingStatus().name()
                ));
    }



    // ✅ Accept booking
    @PatchMapping("/acceptBooking")
    public ResponseEntity<CarBookingResponseDTO> acceptBooking(@RequestParam Long bookingId) {
        CarBooking booking = carBookingService.acceptBooking(bookingId);
        return ResponseEntity.ok(
                new CarBookingResponseDTO(
                        "Car booking accepted successfully",
                        booking.getCar() != null ? booking.getCar().getCarId() : null,
                        booking.getBookingId(),
                        booking.getBookingStatus().name()
                )
        );
    }

    // ✅ Get all pending bookings
    @GetMapping("/getPendingBookings")
    public ResponseEntity<List<CarBooking> > getPendingBookings() {
        return ResponseEntity.ok(carBookingService.getPendingBookings());
    }

    // ✅ Approve booking
    @PatchMapping("/approveBooking")
    public ResponseEntity<CarBookingResponseDTO> approveBooking(@RequestParam Long bookingId) {
        CarBooking booking = carBookingService.approveBooking(bookingId);
        return ResponseEntity.ok(
                new CarBookingResponseDTO(
                        "Car booking approved successfully",
                        booking.getCar() != null ? booking.getCar().getCarId() : null,
                        booking.getBookingId(),
                        booking.getBookingStatus().name()
                )
        );
    }


    // ✅ Reject booking
    @PatchMapping("/rejectBooking")
    public ResponseEntity<CarBookingResponseDTO> rejectBooking(@RequestParam Long bookingId) {
        CarBooking booking = carBookingService.rejectBooking(bookingId);
        return ResponseEntity.ok(new CarBookingResponseDTO(
                "Car booking rejected successfully",
                booking.getCar() != null ? booking.getCar().getCarId() : null,
                booking.getBookingId(),
                booking.getBookingStatus().name()
        ));
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam Long bookingId,
            @RequestBody CarBookingDTO newMessage) {

        carBookingService.addMessage(bookingId, newMessage);
        return ResponseEntity.ok("Message added successfully");
    }

}
