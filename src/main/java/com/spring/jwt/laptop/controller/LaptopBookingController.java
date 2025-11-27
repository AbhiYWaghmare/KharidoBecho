package com.spring.jwt.laptop.controller;

import com.spring.jwt.laptop.dto.BookingResponseDTO;
import com.spring.jwt.laptop.dto.LaptopRequestCreateDTO;
import com.spring.jwt.laptop.dto.LaptopRequestResponseDTO;
import com.spring.jwt.laptop.entity.LaptopBooking;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import com.spring.jwt.laptop.service.LaptopRequestService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final LaptopRequestService service;

    /**
     * Create a new laptop request
     * Returns 201 CREATED on success
     */
    @PostMapping("/create")
    public ResponseEntity<LaptopRequestResponseDTO> create(@Valid @RequestBody LaptopRequestCreateDTO dto) {
        LaptopRequestResponseDTO response = service.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all requests for a specific laptop
     * Returns 200 OK
     */
    @GetMapping("/{laptopId}")
    public ResponseEntity<List<LaptopRequestResponseDTO>> listForLaptop(@PathVariable Long laptopId) {
        return ResponseEntity.ok(service.listRequestsForLaptop(laptopId));
    }

    /**
     * List all requests made by a specific buyer
     * Returns 200 OK
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<LaptopRequestResponseDTO>> listForBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(service.listRequestsForBuyer(buyerId));
    }

    /**
     * Update status (PENDING / ACCEPTED / REJECTED / COMPLETED)
     * Returns 200 OK
     */
    @PatchMapping("/{laptopBookingId}/status")
    public ResponseEntity<LaptopRequestResponseDTO> updateStatus(
            @PathVariable Long laptopBookingId,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateRequestStatus(laptopBookingId, status));
    }

    /**
     * Append a chat message to a laptop request conversation
     * Returns 200 OK
     */
//    @PostMapping("/{laptopBookingId}/message")
//    public ResponseEntity<LaptopRequestResponseDTO> sendMessage(
//            @PathVariable Long laptopBookingId,
//            @RequestParam Long senderUserId,
//            @RequestParam String message) {
//        return ResponseEntity.ok(service.appendMessage(laptopBookingId, senderUserId, message));
//    }

    /**
     * Mark request as completed (sold) and reject others
     * Returns 200 OK
     */
    @PostMapping("/{laptopBookingId}/complete")
    public ResponseEntity<BaseResponseDTO> complete(@PathVariable Long laptopBookingId) {
        service.markRequestCompletedAndMarkSold(laptopBookingId);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .code("200")
                        .message("Marked sold and others rejected")
                        .build()
        );
    }
}

