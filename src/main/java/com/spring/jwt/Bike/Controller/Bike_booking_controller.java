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
            @RequestBody Bike_booking_dto dto
    ) {

        Bike_booking booking = bikeBookingService.createBooking(dto);
        Long sellerId = booking.getBike().getSeller().getSellerId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BookingResponce(
                        "SUCCESS",
                        "Bike booking created successfully",
                        booking.getId(),
                        booking.getBike().getBike_id(),
                        booking.getBuyer().getBuyerId(),
                        sellerId
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
// update status of booking "ACCEPTED,IN_NEGOTIATION"
    @PutMapping("/update/status")
    public ResponseEntity<ApiResponse> updateBookingStatus(
            @RequestParam Long bookingId,
            @RequestParam Bike_booking.BookingStatus status) {
        Bike_booking updated = bikeBookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(
                new ApiResponse("SUCCESS","Status Updated Successfully")
        );
    }


    //  GET bookings by Buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Bike_booking>> getBookingsByBuyer(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(
                bikeBookingService.getBookingsByBuyerId(buyerId)
        );
    }

    //  GET bookings by Seller ID
    @GetMapping("/get-seller/{sellerId}")
    public ResponseEntity<List<Bike_booking>> getBookingsBySeller(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                bikeBookingService.getBookingsBySellerId(sellerId)
        );
    }

    @GetMapping("/bike/{bikeId}")
    public ResponseEntity<List<Bike_booking>> getBookingsByBikeId(
            @PathVariable Long bikeId) {

        return ResponseEntity.ok(
                bikeBookingService.getBookingsByBikeId(bikeId)
        );
    }



}
