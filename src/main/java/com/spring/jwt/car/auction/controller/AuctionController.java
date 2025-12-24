package com.spring.jwt.car.auction.controller;

import com.spring.jwt.car.auction.dto.AuctionDTO;
import com.spring.jwt.car.auction.dto.AuctionResponseDTO;
import com.spring.jwt.car.auction.entity.Auction;
import com.spring.jwt.car.auction.exception.CarAuctionNotFoundException;
import com.spring.jwt.car.auction.mapper.AuctionMapper;
import com.spring.jwt.car.auction.repository.AuctionRepository;
import com.spring.jwt.car.auction.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionRepository AuctionRepository;
    private final AuctionService AuctionService;

    // ========= CREATE AUCTION =========
    @PostMapping("/create")
    public ResponseEntity<AuctionDTO> createAuction(@Valid @RequestBody  AuctionDTO dto) {

        AuctionDTO response = AuctionService.createAuction(dto);

        return ResponseEntity
                .created(URI.create("/api/v1/auctions/" + response.auctionId()))
                .body(response);
    }

    // ========= GET BY ID =========
//    @GetMapping("/{id}")
//    public ResponseEntity<AuctionDTO> getById(@PathVariable Long id) {
//
//        Auction a = AuctionRepository.findById(id)
//                .orElseThrow(() -> new CarAuctionNotFoundException(id));
//
//        return ResponseEntity.ok(AuctionMapper.toDTO(a));
//    }
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponseDTO> getAuction(@PathVariable Long auctionId) {

        Auction auction = AuctionService.getAuctionById(auctionId);


        AuctionResponseDTO response = AuctionResponseDTO.builder()
                .auctionId(auction.getAuctionId())
                .car(auction.getCar())   // ⭐ FULL CAR OBJECT
                .startPrice(auction.getStartPrice())
                .currentPrice(auction.getCurrentPrice())
                .minIncrementInRupees(auction.getMinIncrementInRupees())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .status(auction.getStatus().name())
                .highestBidderUserId(auction.getHighestBidderUserId())
                .version(auction.getVersion())
                .build();

        return ResponseEntity.ok(response);
    }


    // ========= LIST BY STATUS =========
    @GetMapping("/status")
    public ResponseEntity<List<AuctionDTO>> list(@RequestParam(required = false) String status) {

        List<Auction> auctions;

        if (status != null) {
            Auction.Status st = Auction.Status.valueOf(status);
            auctions = AuctionRepository.findAll()
                    .stream()
                    .filter(a -> a.getStatus() == st)
                    .toList();
        } else {
            auctions = AuctionRepository.findAll();
        }

        List<AuctionDTO> dtos = auctions.stream()
                .map(AuctionMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // ========= WINNER ACCEPT / REJECT =========
    @PostMapping("/{auctionId}/winner-accept")
    public ResponseEntity<Void> winnerAccept(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        AuctionService.winnerAccept(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{auctionId}/winner-reject")
    public ResponseEntity<Void> winnerReject(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {

        AuctionService.winnerReject(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    // ========= MANUALLY START & END AUCTIONS =========
    @PostMapping("/_start-due")
    public ResponseEntity<Void> startDue() {
        AuctionService.startDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_end-due")
    public ResponseEntity<Void> endDue() {
        AuctionService.endDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_process-expired-offers")
    public ResponseEntity<Void> processExpiredOffers() {
        AuctionService.processExpiredOffers();
        return ResponseEntity.noContent().build();
    }
}
