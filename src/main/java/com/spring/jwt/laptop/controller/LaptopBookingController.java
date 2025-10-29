package com.spring.jwt.laptop.controller;

import com.spring.jwt.laptop.dto.BookingResponseDTO;
import com.spring.jwt.laptop.dto.LaptopBookingDTO;
import com.spring.jwt.laptop.entity.Booking;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.service.LaptopBookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//********************************************************//

    //Author : Sudhir Lokade
    //Laptop Booking Controller
    //Date : 16/10/2025

//*******************************************************//

@RestController
@RequestMapping("/api/laptopBookings")
@RequiredArgsConstructor
public class LaptopBookingController {
    private final LaptopBookingService laptopBookingService;
    BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();


    //====================================================//
    //  Create Laptop Booking                             //
    //  Post /api/laptopBookings/createBooking            //
    //====================================================//
    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody LaptopBookingDTO laptopBookingDTO, HttpServletRequest request){
        Booking booking = laptopBookingService.createBooking(laptopBookingDTO);
        String apiPath = request.getRequestURI();
        Long laptopId = (booking.getLaptop() != null) ? booking.getLaptop().getId() : null;
        Long bookingId = booking.getId();
        String booKingStatus = booking.getStatus().name();

        bookingResponseDTO.setMessage(booking.getStatus() == Status.PENDING
                ? "Laptop booking created successfully and currently status is pending"
                : "Laptop booked Successfully!");

        String message = bookingResponseDTO.getMessage();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BookingResponseDTO(
                        "CREATED",
                        message,
                        200,
                        LocalDateTime.now(),
                        "NULL",
                        apiPath,
                        laptopId,
                        bookingId,
                        booKingStatus));
    }

    //====================================================//
    //  Get Laptop pending Booking                        //
    //  GET /api/laptopBookings/getPendingBookings        //
    //====================================================//
    @GetMapping("/getPendingBookings")
    public ResponseEntity<List<Booking>> getPendingApis() {
        List<Booking> pendingBookings = laptopBookingService.getPendingBookings();
        return ResponseEntity.ok(pendingBookings);
    }

    //====================================================//
    //  Approve Laptop pending Booking                    //
    //  PATCH /api/laptopBookings/approveBooking          //
    //====================================================//
    @PatchMapping("/approveBooking")
    public ResponseEntity<BookingResponseDTO> approveBookings(@RequestParam Long booking_Id, HttpServletRequest request){
        Booking approvedBooking = laptopBookingService.approveBooking(booking_Id);

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setMessage("Booking approved successfully");
        responseDTO.setStatusCode(200);
        responseDTO.setTimeStamp(LocalDateTime.now());
        responseDTO.setApiPath(request.getRequestURI());
        responseDTO.setLaptopId(approvedBooking.getLaptop() != null ? approvedBooking.getLaptop().getId() : null);
        responseDTO.setBookingId(approvedBooking.getId());
        responseDTO.setBookingStatus(approvedBooking.getStatus().name());

        return ResponseEntity.ok(responseDTO);
    }

    //====================================================//
    //  Reject Laptop Booking                             //
    //  PATCH /api/laptopBookings/rejectBooking           //
    //====================================================//
    @PatchMapping("/rejectBooking")
    public ResponseEntity<BookingResponseDTO> rejectBooking(@RequestParam Long bookingId, HttpServletRequest request) {
        Booking rejectBooking = laptopBookingService.rejectBooking(bookingId);

        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setMessage("Booking rejected successfully");
        responseDTO.setStatusCode(200);
        responseDTO.setTimeStamp(LocalDateTime.now());
        responseDTO.setApiPath(request.getRequestURI());
        responseDTO.setLaptopId(rejectBooking.getLaptop() != null ? rejectBooking.getLaptop().getId() : null);
        responseDTO.setBookingId(rejectBooking.getId());
        responseDTO.setBookingStatus(rejectBooking.getStatus().name());

        return ResponseEntity.ok(responseDTO);
    }

}