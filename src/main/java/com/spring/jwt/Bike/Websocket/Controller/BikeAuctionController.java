//package com.spring.jwt.Bike.Websocket.Controller;
//
//import com.spring.jwt.Bike.Websocket.Dto.BikeBidDto;
//import com.spring.jwt.Bike.Websocket.Dto.CreateBikeAuctionRequestDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeAuction;
//import com.spring.jwt.Bike.Websocket.Service.BikeAuctionService;
//import com.spring.jwt.Bike.Websocket.Repository.BikeAuctionRepository;
//import com.spring.jwt.exception.ResourceNotFoundException;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/bikes/auctions")
//@RequiredArgsConstructor
//public class BikeAuctionController {
//
//    private final BikeAuctionRepository bikeAuctionRepository;
//    private final BikeAuctionService bikeAuctionService;
//
//    // =====================================================
//    //                CREATE BIKE AUCTION
//    // =====================================================
////    @PostMapping("/create")
////    public ResponseEntity<BikeAuction> createBikeAuction(@RequestBody BikeAuction dto) {
////
////        BikeAuction a = new BikeAuction();
////        a.setBikeId(dto.getBikeId());
////        a.setStartPrice(dto.getStartPrice());
////        a.setCurrentPrice(dto.getStartPrice());
////        a.setMinIncrement(dto.getMinIncrement());
////        a.setStartTime(dto.getStartTime() != null ? dto.getStartTime() : LocalDateTime.now());
////        a.setEndTime(dto.getEndTime());
////        a.setStatus(BikeAuction.AuctionStatus.SCHEDULED);
////        a.setHighestBidderUserId(null);
////
////        BikeAuction saved = bikeAuctionRepository.save(a);
////
////        return ResponseEntity.created(URI.create("/bikes/auction/" + saved.getAuctionId()))
////                .body(saved);
////    }
//
////    @PostMapping("/create")
////    public ResponseEntity<BikeAuction> createBikeAuction(@RequestBody BikeAuction dto) {
////
////        BikeAuction saved = bikeAuctionService.createAuction(dto);
////
////        return ResponseEntity
////                .created(URI.create("/bikes/auction/" + saved.getAuctionId()))
////                .body(saved);
////    }
//@PostMapping("/create")
//public ResponseEntity<BikeAuction> createBikeAuction(@Valid
//        @RequestBody CreateBikeAuctionRequestDto dto) {
//
//    BikeAuction saved = bikeAuctionService.createAuction(dto);
//
//    return ResponseEntity
//            .created(URI.create("/bikes/auction/" + saved.getAuctionId()))
//            .body(saved);
//}
//
//
//    // =====================================================
//    //                  GET BY ID
//    // =====================================================
////    @GetMapping("/{id}")
////    public ResponseEntity<BikeAuction> getById(@PathVariable Long id) {
////
////        BikeAuction a = bikeAuctionRepository.findById(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Bike Auction not found: " + id));
////
////        return ResponseEntity.ok(a);
////    }
//    @GetMapping("/{id}")
//    public ResponseEntity<BikeAuction> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(bikeAuctionService.getAuctionById(id));
//    }
//
//
//    // =====================================================
//    //                 LIST BY STATUS
//    // =====================================================
////    @GetMapping("/status")
////    public ResponseEntity<List<BikeAuction>> list(@RequestParam(required = false) String status) {
////
////        List<BikeAuction> auctions;
////
////        if (status != null) {
////            BikeAuction.AuctionStatus st = BikeAuction.AuctionStatus.valueOf(status);
////            auctions = bikeAuctionRepository.findAll()
////                    .stream()
////                    .filter(a -> a.getStatus() == st)
////                    .toList();
////        } else {
////            auctions = bikeAuctionRepository.findAll();
////        }
////
////        return ResponseEntity.ok(auctions);
////    }
//    @GetMapping("/status")
//    public ResponseEntity<List<BikeAuction>> list(@RequestParam(required = false) String status) {
//        return ResponseEntity.ok(bikeAuctionService.listAuctionsByStatus(status));
//    }
//
//    // =====================================================
//    //                 PLACE BID
//    // =====================================================
//    @PostMapping("/{auctionId}/place-bid")
//    public ResponseEntity<BikeBidDto> placeBid(
//            @PathVariable Long auctionId,
//            @RequestParam Long userId,
//            @RequestParam("amount") String amount
//    ) {
//        BikeBidDto bid = bikeAuctionService.placeBid(
//                auctionId,
//                userId,
//                new java.math.BigDecimal(amount)
//        );
//
//        return ResponseEntity.ok(bid);
//    }
//
//    // =====================================================
//    //                 WINNER ACCEPT
//    // =====================================================
//    @PostMapping("/{auctionId}/winner-accept")
//    public ResponseEntity<Void> winnerAccept(
//            @PathVariable Long auctionId,
//            @RequestParam Long userId
//    ) {
//        bikeAuctionService.winnerAccept(auctionId, userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    // =====================================================
//    //                 WINNER REJECT
//    // =====================================================
//    @PostMapping("/{auctionId}/winner-reject")
//    public ResponseEntity<Void> winnerReject(
//            @PathVariable Long auctionId,
//            @RequestParam Long userId
//    ) {
//        bikeAuctionService.winnerReject(auctionId, userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    // =====================================================
//    //              MANUAL DEBUG ENDPOINTS
//    // =====================================================
//    @PostMapping("/_start-due")
//    public ResponseEntity<Void> startDue() {
//        bikeAuctionService.startDueAuctions();
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/_end-due")
//    public ResponseEntity<Void> endDue() {
//        bikeAuctionService.endDueAuctions();
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/_process-expired-offers")
//    public ResponseEntity<Void> processExpiredOffers() {
//        bikeAuctionService.processExpiredOffers();
//        return ResponseEntity.noContent().build();
//    }
//}
//
