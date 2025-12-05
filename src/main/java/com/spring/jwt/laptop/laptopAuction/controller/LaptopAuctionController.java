package com.spring.jwt.laptop.laptopAuction.controller;

import com.spring.jwt.exception.laptop.LaptopAuctionNotFoundException;
import com.spring.jwt.laptop.laptopAuction.dto.LaptopAuctionDTO;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;
import com.spring.jwt.laptop.laptopAuction.mapper.LaptopAuctionMapper;
import com.spring.jwt.laptop.laptopAuction.repository.LaptopAuctionRepository;
import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
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

    private final LaptopAuctionRepository laptopAuctionRepository;
    private final LaptopAuctionService laptopAuctionService;

    // ========= CREATE AUCTION =========
    @PostMapping("/create")
    public ResponseEntity<LaptopAuctionDTO> createLaptopAuction(@RequestBody LaptopAuctionDTO dto) {

        LaptopAuction a = new LaptopAuction();
        a.setLaptopId(dto.laptopId());
        a.setStartPrice(dto.startPrice());
        a.setCurrentPrice(dto.startPrice()); // initialize
        a.setMinIncrementInRupees(dto.minIncrementInRupees());
        a.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
        a.setEndTime(dto.endTime());
        a.setStatus(LaptopAuction.AuctionStatus.SCHEDULED);
        a.setHighestBidderUserId(null);

        LaptopAuction saved = laptopAuctionRepository.save(a);

        LaptopAuctionDTO response = LaptopAuctionMapper.toDTO(saved);

        return ResponseEntity.created(URI.create("/api/v1/laptop-auctions/" + saved.getAuctionId()))
                .body(response);
    }

    // ========= GET BY ID =========
    @GetMapping("/{id}")
    public ResponseEntity<LaptopAuctionDTO> getById(@PathVariable Long id) {

        LaptopAuction a = laptopAuctionRepository.findById(id)
                .orElseThrow(() -> new LaptopAuctionNotFoundException(id));

        return ResponseEntity.ok(LaptopAuctionMapper.toDTO(a));
    }

    // ========= LIST BY STATUS =========
    @GetMapping("/status")
    public ResponseEntity<List<LaptopAuctionDTO>> list(
            @RequestParam(required = false) String status) {

        List<LaptopAuction> auctions;

        if (status != null) {
            LaptopAuction.AuctionStatus st = LaptopAuction.AuctionStatus.valueOf(status);
            auctions = laptopAuctionRepository.findAll()
                    .stream()
                    .filter(a -> a.getStatus() == st)
                    .toList();
        } else {
            auctions = laptopAuctionRepository.findAll();
        }

        List<LaptopAuctionDTO> dtos = auctions.stream()
                .map(LaptopAuctionMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // ========= WINNER ACCEPT =========
    @PostMapping("/{auctionId}/winner-accept")
    public ResponseEntity<Void> winnerAccept(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        laptopAuctionService.winnerAccept(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    // ========= WINNER REJECT =========
    @PostMapping("/{auctionId}/winner-reject")
    public ResponseEntity<Void> winnerReject(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        laptopAuctionService.winnerReject(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    // ========= MANUAL DEBUG ENDPOINTS =========
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
