package com.spring.jwt.Bike.Controller;

import com.spring.jwt.Bike.Entity.Bike_booking;
import com.spring.jwt.Bike.Service.Bike_booking_service;
import com.spring.jwt.Bike.dto.Bike_booking_dto;
import com.spring.jwt.Bike.response.ApiResponse;
import com.spring.jwt.Bike.response.BookingResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/bikes/bookings")
public class Bike_booking_controller {

    @Autowired
    Bike_booking_service bikeBookingService;


    @PostMapping("/post")
    public ResponseEntity<BookingResponce> createBooking(
            @RequestParam Long bikeId,
            @RequestParam Long buyerId,
            @RequestParam(required = false) String message
            ) {

        Bike_booking_dto dto = new Bike_booking_dto();
        dto.setBikeId(bikeId);
        dto.setBuyerId(buyerId);
        dto.setMessage(message);

        Bike_booking booking = bikeBookingService.createBooking(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BookingResponce(
                        "SUCCESS",
                        "Bike booking created successfully",
                        booking.getId(),
                        booking.getBike().getBike_id(),
                        booking.getBuyer().getBuyerId()
                ));
    }





    @GetMapping("/get/pending")
    public ResponseEntity<List<Bike_booking>> getBookings() {
        List<Bike_booking> bookings = bikeBookingService.getBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/get/booking")
    public ResponseEntity<Bike_booking> getBookingById(@RequestParam Long bookingid) {
        return ResponseEntity.ok(bikeBookingService.getBookingById(bookingid));
    }







    @PutMapping("/complete")
    public ResponseEntity<ApiResponse> completeBooking(@RequestParam Long bookingId) {
        bikeBookingService.completeBooking(bookingId);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Booking SOLD successfully"));
    }

    @PutMapping("/reject")
    public ResponseEntity<ApiResponse> rejectBooking(@RequestParam Long bookingId) {
        bikeBookingService.rejectBooking(bookingId);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Booking rejected successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteBooking(@RequestParam Long bookingId) {
        bikeBookingService.deleteBooking(bookingId);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Booking deleted successfully"));
    }


    @PostMapping("/chat/send")
    public ResponseEntity<ApiResponse> sendMessage(
            @RequestParam Long bookingId,
            @RequestBody Bike_booking_dto conversation) {

        bikeBookingService.addMessage(bookingId, conversation);

        return ResponseEntity.ok(
                new ApiResponse("SUCCESS", "Message added successfully")
        );
    }

    @PutMapping("/update/status")
    public ResponseEntity<ApiResponse> updateBookingStatus(
            @RequestParam Long bookingId,
            @RequestParam Bike_booking.BookingStatus status) {
        Bike_booking updated = bikeBookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(
                new ApiResponse("SUCCESS","Status Updated Successfully")
        );
    }





}
