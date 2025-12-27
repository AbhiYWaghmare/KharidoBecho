package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mobile/requests")
@RequiredArgsConstructor
public class MobileRequestController {

    private final MobileRequestService service;

    /**
     * Create a new mobile request
     * Returns 201 CREATED on success
     */
    @PostMapping("/create")
    public ResponseEntity<MobileRequestResponseDTO> create(@RequestBody MobileRequestCreateDTO dto) {
        MobileRequestResponseDTO response = service.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all requests for a specific mobile
     * Returns 200 OK
     */
    @GetMapping("/{mobileId}")
    public ResponseEntity<List<MobileRequestResponseDTO>> listForMobile(@PathVariable Long mobileId) {
        return ResponseEntity.ok(service.listRequestsForMobile(mobileId));
    }

    /**
     * List all requests made by a specific buyer
     * Returns 200 OK
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<MobileRequestResponseDTO>> listForBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(service.listRequestsForBuyer(buyerId));
    }

    /**
     * Update status (PENDING / ACCEPTED / REJECTED / COMPLETED)
     * Returns 200 OK
     */

//    @GetMapping("/seller/{sellerId}")
//    public ResponseEntity<List<MobileRequestDTO>> getRequestsBySeller(@PathVariable Long sellerId) {
//        return ResponseEntity.ok(mobileRequestService.getRequestsBySeller(sellerId));
//    }




    @PatchMapping("/{requestId}/status")
    public ResponseEntity<MobileRequestResponseDTO> updateStatus(
            @PathVariable Long requestId,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateRequestStatus(requestId, status));
    }

    /**
     * Append a chat message to a mobile request conversation
     * Returns 200 OK
     */
    @PostMapping("/{requestId}/message")
    public ResponseEntity<MobileRequestResponseDTO> sendMessage(
            @PathVariable Long requestId,
            @RequestParam Long senderUserId,
            @RequestParam String message) {
        return ResponseEntity.ok(service.appendMessage(requestId, senderUserId, message));
    }

    /**
     * Mark request as completed (sold) and reject others
     * Returns 200 OK
     */
    @PostMapping("/{requestId}/complete")
    public ResponseEntity<BaseResponseDTO> complete(@PathVariable Long requestId) {
        service.markRequestCompletedAndMarkSold(requestId);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .code("200")
                        .message("Marked sold and others rejected")
                        .build()
        );
    }
}
