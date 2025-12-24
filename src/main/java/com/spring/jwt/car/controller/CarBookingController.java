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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carBookings")
@RequiredArgsConstructor
public class CarBookingController {

    private final CarBookingService carBookingService;

    // ✅ Create booking

    @PostMapping("/createBooking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody CarBookingDTO carBookingDTO) {

        CarBooking booking = carBookingService.createBooking(carBookingDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", booking.getBookingId());
        response.put("carId", booking.getCar().getCarId());
        response.put("bookingStatus", booking.getBookingStatus().name());
        response.put("conversation", booking.getConversation());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

        CarBooking updatedBooking = carBookingService.addMessage(bookingId, newMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", updatedBooking.getBookingId());
        response.put("bookingStatus", updatedBooking.getBookingStatus().name());
        response.put("conversation", updatedBooking.getConversation());

        return ResponseEntity.ok(response);
    }

    // 🔵 Get all bookings by buyerId (Chat List)
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getBookingsByBuyer(@PathVariable Long buyerId) {

        List<CarBooking> bookings = carBookingService.getBookingsByBuyerId(buyerId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "All Car Requests Fetched Successfully");
        response.put("count", bookings.size());
        response.put("data", bookings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CarBooking>> getBookingsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(carBookingService.getBookingsBySellerId(sellerId));
    }



    // 🔵 Get single booking + conversation (Chat Thread)
    @GetMapping("/{bookingId}")
    public ResponseEntity<CarBooking> getBookingDetails(@PathVariable Long bookingId) {
        return ResponseEntity.ok(carBookingService.getBookingDetails(bookingId));
    }


}
