package com.spring.jwt.auction.controller;

import com.spring.jwt.auction.dto.AuctionDTO;
import com.spring.jwt.auction.dto.AuctionRequestDTO;
import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.exception.AuctionNotFoundException;
import com.spring.jwt.auction.mapper.AuctionMapper;
import com.spring.jwt.auction.repository.AuctionRepository;
import com.spring.jwt.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    // CREATE AUCTION
    @PostMapping("/create")
    public ResponseEntity<AuctionDTO> createAuction(@RequestBody AuctionRequestDTO dto) {

        AuctionDTO response = auctionService.createAuction(dto);

        return ResponseEntity
                .created(URI.create("/api/v1/auctions/" + response.auctionId()))
                .body(response);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AuctionDTO> getById(@PathVariable Long id) {
        Auction a = auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));

        return ResponseEntity.ok(AuctionMapper.toDTO(a));
    }

    // ========= LIST BY STATUS =========
    @GetMapping("/status")
    public ResponseEntity<List<AuctionDTO>> list(
            @RequestParam(required = false) String status) {

        List<Auction> auctions;

        if (status != null) {
            Auction.Status st = Auction.Status.valueOf(status);
            auctions = auctionRepository.findAll()
                    .stream()
                    .filter(a -> a.getStatus() == st)
                    .toList();
        } else {
            auctions = auctionRepository.findAll();
        }

        List<AuctionDTO> dtos = auctions.stream()
                .map(AuctionMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // WINNER ACCEPT / REJECT
    // typically the winner (buyer) will call these

    @PostMapping("/{auctionId}/winner-accept")
    public ResponseEntity<Void> winnerAccept(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {
        auctionService.winnerAccept(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{auctionId}/winner-reject")
    public ResponseEntity<Void> winnerReject(
            @PathVariable Long auctionId,
            @RequestParam Long userId) {
        auctionService.winnerReject(auctionId, userId);
        return ResponseEntity.noContent().build();
    }

    //  we can manually start & end the Aunction endpoints
    @PostMapping("/_start-due")
    public ResponseEntity<Void> startDue() {
        auctionService.startDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_end-due")
    public ResponseEntity<Void> endDue() {
        auctionService.endDueAuctions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_process-expired-offers")
    public ResponseEntity<Void> processExpiredOffers() {
        auctionService.processExpiredOffers();
        return ResponseEntity.noContent().build();
    }
}

