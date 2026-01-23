//package com.spring.jwt.car.auction.controller;
//
//import com.spring.jwt.auction.exception.AuctionNotFoundException;
//import com.spring.jwt.car.auction.dto.CarAuctionDTO;
//import com.spring.jwt.car.auction.dto.CarAuctionResponseDTO;
//import com.spring.jwt.car.auction.entity.CarAuction;
//import com.spring.jwt.car.auction.mapper.CarAuctionMapper;
//import com.spring.jwt.car.auction.repository.CarAuctionRepository;
//import com.spring.jwt.car.auction.service.CarAuctionService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/car-auctions")
//@RequiredArgsConstructor
//public class CarAuctionController {
//
//    private final CarAuctionRepository carAuctionRepository;
//    private final CarAuctionService carAuctionService;
//
//    // ========= CREATE AUCTION =========
//    @PostMapping("/create")
//    public ResponseEntity<CarAuctionDTO> createAuction(@Valid @RequestBody CarAuctionDTO dto) {
//
//        CarAuctionDTO response = carAuctionService.createAuction(dto);
//
//        return ResponseEntity
//                .created(URI.create("/api/v1/car-auctions/" + response.auctionId()))
//                .body(response);
//    }
//
//    // ========= GET BY ID =========
////    @GetMapping("/{id}")
////    public ResponseEntity<AuctionDTO> getById(@PathVariable Long id) {
////
////        Auction a = AuctionRepository.findById(id)
////                .orElseThrow(() -> new CarAuctionNotFoundException(id));
////
////        return ResponseEntity.ok(AuctionMapper.toDTO(a));
////    }
//    @GetMapping("/{auctionId}")
//    public ResponseEntity<CarAuctionResponseDTO> getAuction(@PathVariable Long auctionId) {
//
//        CarAuction auction = carAuctionService.getAuctionById(auctionId);
//
//        CarAuctionResponseDTO response = CarAuctionResponseDTO.builder()
//                .auctionId(auction.getAuctionId())
//                .car(auction.getCar())   // ⭐ FULL CAR OBJECT
//                .startPrice(auction.getStartPrice())
//                .currentPrice(auction.getCurrentPrice())
//                .minIncrementInRupees(auction.getMinIncrementInRupees())
//                .startTime(auction.getStartTime())
//                .endTime(auction.getEndTime())
//                .status(auction.getStatus().name())
//                .highestBidderUserId(auction.getHighestBidderUserId())
//                .version(auction.getVersion())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//
//
//    // ========= LIST BY STATUS =========
//    @GetMapping("/status")
//    public ResponseEntity<List<CarAuctionDTO>> list(@RequestParam(required = false) String status) {
//
//        List<CarAuction> auctions;
//
//        if (status != null) {
//            CarAuction.Status st = CarAuction.Status.valueOf(status);
//            auctions = carAuctionRepository.findAll()
//                    .stream()
//                    .filter(a -> a.getStatus() == st)
//                    .toList();
//        } else {
//            auctions = carAuctionRepository.findAll();
//        }
//
//        List<CarAuctionDTO> dtos = auctions.stream()
//                .map(CarAuctionMapper::toDTO)
//                .toList();
//
//        return ResponseEntity.ok(dtos);
//    }
//
//    // ========= WINNER ACCEPT / REJECT =========
//    @PostMapping("/{auctionId}/winner-accept")
//    public ResponseEntity<Void> winnerAccept(
//            @PathVariable Long auctionId,
//            @RequestParam Long userId) {
//
//        carAuctionService.winnerAccept(auctionId, userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{auctionId}/winner-reject")
//    public ResponseEntity<?> winnerReject(
//            @PathVariable Long auctionId,
//            @RequestParam Long userId) {
//
//        carAuctionService.winnerReject(auctionId, userId);
//
//        return ResponseEntity.ok("Car auction winner rejected");
//    }
//
//
//    // ========= MANUALLY START & END AUCTIONS =========
//    @PostMapping("/_start-due")
//    public ResponseEntity<Void> startDue() {
//        carAuctionService.startDueAuctions();
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/_end-due")
//    public ResponseEntity<Void> endDue() {
//        carAuctionService.endDueAuctions();
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/_process-expired-offers")
//    public ResponseEntity<Void> processExpiredOffers() {
//        carAuctionService.processExpiredOffers();
//        return ResponseEntity.noContent().build();
//    }
//}
