//package com.spring.jwt.auction.controller;
//
//import com.spring.jwt.auction.service.AuctionService;
//import com.spring.jwt.utils.BaseResponseDTO;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/auctions")
//@RequiredArgsConstructor
//public class AuctionRestController {
//
//    private final AuctionService auctionService;
//
//    @PostMapping("/create")
//    public ResponseEntity<AuctionResponseDTO> create(@Valid @RequestBody AuctionCreateDTO dto) {
//        AuctionResponseDTO resp = auctionService.createAuction(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
//    }
//
//    @PostMapping("/{id}/finalize")
//    public ResponseEntity<BaseResponseDTO> finalize(@PathVariable Long id) {
//        auctionService.finalizeAuction(id);
//        return ResponseEntity.ok(BaseResponseDTO.builder().code("200").message("Finalize triggered").build());
//    }
//
//    @PostMapping("/{id}/confirm")
//    public ResponseEntity<BaseResponseDTO> confirm(@PathVariable Long id, @RequestParam Long bidderUserId) {
//        auctionService.confirmOffer(id, bidderUserId);
//        return ResponseEntity.ok(BaseResponseDTO.builder().code("200").message("Offer confirmed").build());
//    }
//
//    @PostMapping("/{id}/reject")
//    public ResponseEntity<BaseResponseDTO> reject(@PathVariable Long id, @RequestParam Long bidderUserId) {
//        // find pending -> the service rejects and promotes next
//        // We'll locate the bid in service by auction id & bidder id
//        // For convenience, we will find top bids in service
//        // Here we just forward to the scheduler-like logic inside service
//        // Reuse rejectOfferAndPromoteNext - but we need Bid instance; easier: call finalize logic to pick next
//        throw new UnsupportedOperationException("Use /api/v1/auctions/{id}/reject?bidderUserId=... implemented in service; implement as needed.");
//    }
//}
