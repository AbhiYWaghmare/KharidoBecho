package com.spring.jwt.auction.controller;

import com.spring.jwt.auction.entity.CarAuction;
import com.spring.jwt.auction.dto.CarAuctionDTO;
import com.spring.jwt.auction.exception.CarAuctionNotFoundException;
import com.spring.jwt.auction.mapper.CarAuctionMapper;
import com.spring.jwt.auction.repository.CarAuctionRepository;
import com.spring.jwt.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/car-auctions")
@RequiredArgsConstructor
public class CarAuctionController {

    private final CarAuctionRepository carAuctionRepository;
    private final AuctionService carAuctionService;

    @PostMapping("/create")
    public ResponseEntity<CarAuctionDTO> createAuction(@RequestBody CarAuctionDTO dto) {
        CarAuction a = new CarAuction();
        a.setCarId(dto.carId());
        a.setStartPrice(dto.startPrice());
        a.setCurrentPrice(dto.startPrice());
        a.setMinIncrementInRupees(dto.minIncrementInRupees());
        a.setStartTime(dto.startTime() != null ? dto.startTime() : OffsetDateTime.now());
        a.setEndTime(dto.endTime());
        a.setStatus(CarAuction.Status.SCHEDULED);
        a.setHighestBidderUserId(null);

        CarAuction saved = carAuctionRepository.save(a);
        CarAuctionDTO response = CarAuctionMapper.toDTO(saved);
        return ResponseEntity
                .created(URI.create("/api/v1/car-auctions/" + saved.getAuctionId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarAuctionDTO> getById(@PathVariable Long id) {
        CarAuction a = carAuctionRepository.findById(id)
                .orElseThrow(() -> new CarAuctionNotFoundException(id));
        return ResponseEntity.ok(CarAuctionMapper.toDTO(a));
    }

    @GetMapping("/status")
    public ResponseEntity<List<CarAuctionDTO>> list(@RequestParam(required = false) String status) {
        List<CarAuction> auctions;
        if (status != null) {
            CarAuction.Status st = CarAuction.Status.valueOf(status);
            auctions = carAuctionRepository.findAll()
                    .stream()
                    .filter(a -> a.getStatus() == st)
                    .toList();
        } else {
            auctions = carAuctionRepository.findAll();
        }
        List<CarAuctionDTO> dtos = auctions.stream()
                .map(CarAuctionMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{auctionId}/winner-accept")
    public ResponseEntity<Void> winnerAccept(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {
        carAuctionService.winnerAccept(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{auctionId}/winner-reject")
    public ResponseEntity<Void> winnerReject(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {
        carAuctionService.winnerReject(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_start-due")
    public ResponseEntity<Void> startDue() {
        carAuctionService.startDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_end-due")
    public ResponseEntity<Void> endDue() {
        carAuctionService.endDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_process-expired-offers")
    public ResponseEntity<Void> processExpiredOffers() {
        carAuctionService.processExpiredOffers();
        return ResponseEntity.noContent().build();
    }
}
