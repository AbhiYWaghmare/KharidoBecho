package com.spring.jwt.Bike.Controller;

import com.spring.jwt.Bike.Entity.BikeBidding;
import com.spring.jwt.Bike.Service.BikeAuctionService;
import com.spring.jwt.Bike.dto.BikeBidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/bikes/auction")
@RequiredArgsConstructor
public class BikeAuctionController {
    private final BikeAuctionService bikeAuctionService;

//    @PostMapping("/start")
//    public ResponseEntity<BikeBidDto> startAuction(@RequestParam Long bikeId,
//                                                   @RequestParam String closingTime) {
//        LocalDateTime closingAt = LocalDateTime.parse(closingTime.trim());
//        BikeBidDto dto = bikeAuctionService.addBikeToAuction(bikeId, closingAt);
//        return ResponseEntity.ok(dto);
//    }

    /**
     * Get auction status by bikeId
     * Example GET: /bikes/auction/get/1
     */
//    @GetMapping("/get/{bikeId}")
//    public ResponseEntity<BikeBidDto> getAuctionByBikeId(@PathVariable Long bikeId) {
//        BikeBidDto dto = bikeAuctionService.getAuctionByBikeId(bikeId);
//        return ResponseEntity.ok(dto);
//    }
    @PostMapping("/create/{bikeId}/{minutes}")
    public BikeBidDto createAuction(
            @PathVariable Long bikeId,
            @PathVariable int minutes
    ) {
        return bikeAuctionService.createAuction(bikeId, minutes);
    }
//    @GetMapping("/get")
//    public BikeBidDto getAuctionByBikeId(@RequestParam Long bikeId){
//        return bikeAuctionService.getAuctionByBikeId(bikeId);
//    }

    //   Get auction by bikeId
//    @GetMapping("/bike/{bikeId}")
//    public BikeBidDto getAuctionByBikeId(@PathVariable Long bikeId) {
//        return bikeAuctionService.getAuctionByBikeId(bikeId);
//    }

    //   Place bid
    @PostMapping("/bid/{biddingId}")
    public BikeBidDto placeBid(
            @PathVariable Integer biddingId,
            @RequestParam Long userId,
            @RequestParam Double amount
    ) {
        return bikeAuctionService.placeBid(biddingId, userId, amount);
    }

    //   Close auction manually
    @PostMapping("/close/{biddingId}")
    public String closeAuction(@PathVariable Integer biddingId) {
        bikeAuctionService.closeAuction(biddingId);
        return "Auction closed successfully";
    }
}
