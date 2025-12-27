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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carBookings")
@RequiredArgsConstructor
public class CarBookingController {

    private final CarBookingService carBookingService;

    // ✅ Create booking
    @PostMapping("/createBooking")
    public ResponseEntity<CarBookingResponseDTO> createBooking(
            @Valid @RequestBody CarBookingDTO carBookingDTO) {

        CarBooking booking = carBookingService.createBooking(carBookingDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                buildResponse("Booking created successfully", booking)
        );
    }

    // ✅ Accept booking
    @PatchMapping("/acceptBooking")
    public ResponseEntity<CarBookingResponseDTO> acceptBooking(
            @RequestParam Long bookingId) {

        CarBooking booking = carBookingService.acceptBooking(bookingId);

        return ResponseEntity.ok(
                buildResponse("Car booking accepted successfully", booking)
        );
    }

    // ✅ Approve booking
    @PatchMapping("/approveBooking")
    public ResponseEntity<CarBookingResponseDTO> approveBooking(
            @RequestParam Long bookingId) {

        CarBooking booking = carBookingService.approveBooking(bookingId);

        return ResponseEntity.ok(
                buildResponse("Car booking approved successfully", booking)
        );
    }

    // ✅ Reject booking
    @PatchMapping("/rejectBooking")
    public ResponseEntity<CarBookingResponseDTO> rejectBooking(
            @RequestParam Long bookingId) {

        CarBooking booking = carBookingService.rejectBooking(bookingId);

        return ResponseEntity.ok(
                buildResponse("Car booking rejected successfully", booking)
        );
    }

    // ✅ Get all pending bookings
    @GetMapping("/getPendingBookings")
    public ResponseEntity<List<CarBookingResponseDTO>> getPendingBookings() {

        List<CarBooking> bookings = carBookingService.getPendingBookings();

        List<CarBookingResponseDTO> response = bookings.stream()
                .map(b -> buildResponse("Pending booking fetched", b))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Send message
    @PostMapping("/send")
    public ResponseEntity<CarBookingResponseDTO> sendMessage(
            @RequestParam Long bookingId,
            @RequestBody CarBookingDTO newMessage) {

        CarBooking booking = carBookingService.addMessage(bookingId, newMessage);

        return ResponseEntity.ok(
                buildResponse("Message sent successfully", booking)
        );
    }

    // 🔵 Get all bookings by buyerId (Chat List)
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<CarBookingResponseDTO>> getBookingsByBuyer(
            @PathVariable Long buyerId) {

        List<CarBooking> bookings =
                carBookingService.getBookingsByBuyerId(buyerId);

        List<CarBookingResponseDTO> response = bookings.stream()
                .map(b -> buildResponse("Booking fetched successfully", b))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🔵 Get all bookings by sellerId
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CarBookingResponseDTO>> getBookingsBySeller(
            @PathVariable Long sellerId) {

        List<CarBooking> bookings =
                carBookingService.getBookingsBySellerId(sellerId);

        List<CarBookingResponseDTO> response = bookings.stream()
                .map(b -> buildResponse("Booking fetched successfully", b))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🔵 Get single booking + conversation (Chat Thread)
    @GetMapping("/{bookingId}")
    public ResponseEntity<CarBookingResponseDTO> getBookingDetails(
            @PathVariable Long bookingId) {

        CarBooking booking =
                carBookingService.getBookingDetails(bookingId);

        return ResponseEntity.ok(
                buildResponse("Booking fetched successfully", booking)
        );
    }

    // 🔧 Common response builder (clean & reusable)
    private CarBookingResponseDTO buildResponse(String message, CarBooking booking) {

        return new CarBookingResponseDTO(
                message,
                booking.getCar() != null ? booking.getCar().getCarId() : null,
                booking.getBookingId(),
                booking.getBookingStatus().name(),
                booking.getConversation()
        );
    }
}
