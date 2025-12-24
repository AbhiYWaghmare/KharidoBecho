package com.spring.jwt.laptop.laptopAuction.controller;

import com.spring.jwt.exception.laptop.LaptopAuctionNotFoundException;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.laptopAuction.dto.LaptopAuctionDTO;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;
import com.spring.jwt.laptop.laptopAuction.mapper.LaptopAuctionMapper;
import com.spring.jwt.laptop.laptopAuction.repository.LaptopAuctionRepository;
import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
import com.spring.jwt.laptop.repository.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/laptop-auctions")
@RequiredArgsConstructor
public class LaptopAuctionController {

    private final LaptopAuctionService laptopAuctionService;

    //====================================================//
    //  Create Auction                                    //
    //  POST /api/v1/laptop-auctions/create               //
    //====================================================//
    @PostMapping("/create")
    public ResponseEntity<LaptopAuctionDTO> createAuction(@RequestBody LaptopAuctionDTO dto) {

        LaptopAuctionDTO created = laptopAuctionService.createAuction(dto);

        return ResponseEntity
                .created(URI.create("/api/v1/laptop-auctions/" + created.auctionId()))
                .body(created);
    }

    //====================================================//
    //  Get Auction By ID                                 //
    //  GET /api/v1/laptop-auctions/{id}                  //
    //====================================================//
    @GetMapping("/{id}")
    public ResponseEntity<LaptopAuctionDTO> getById(@PathVariable Long id) {
        LaptopAuctionDTO dto = laptopAuctionService.getById(id);
        return ResponseEntity.ok(dto);
    }

    //====================================================//
    //  List Auctions By Status                           //
    //  GET /api/v1/laptop-auctions/status?status=RUNNING //
    //====================================================//
    @GetMapping("/status")
    public ResponseEntity<List<LaptopAuctionDTO>> listByStatus(
            @RequestParam(required = false) String status) {

        List<LaptopAuctionDTO> auctions = laptopAuctionService.listByStatus(status);
        return ResponseEntity.ok(auctions);
    }

    //====================================================//
    //  Winner Accept                                      //
    //  POST /api/v1/laptop-auctions/{auctionId}/winner-accept
    //====================================================//
    @PostMapping("/{auctionId}/winner-accept")
    public ResponseEntity<Void> winnerAccept(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        laptopAuctionService.winnerAccept(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    //====================================================//
    //  Winner Reject                                      //
    //  POST /api/v1/laptop-auctions/{auctionId}/winner-reject
    //====================================================//
    @PostMapping("/{auctionId}/winner-reject")
    public ResponseEntity<Void> winnerReject(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        laptopAuctionService.winnerReject(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    //====================================================//
    //  Admin Debug Endpoints                              //
    //====================================================//

    @PostMapping("/_start-due")
    public ResponseEntity<Void> startDue() {
        laptopAuctionService.startDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_end-due")
    public ResponseEntity<Void> endDue() {
        laptopAuctionService.endDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_process-expired-offers")
    public ResponseEntity<Void> processExpiredOffers() {
        laptopAuctionService.processExpiredOffers();
        return ResponseEntity.noContent().build();
    }
}
