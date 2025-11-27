package com.spring.jwt.laptop.controller;

import com.spring.jwt.laptop.dto.BeadingLaptopDTO;
import com.spring.jwt.laptop.dto.BeadingLaptopWithInsDTO;
import com.spring.jwt.laptop.dto.BidLaptopDTO;
import com.spring.jwt.laptop.dto.BidResponseDTO;
import com.spring.jwt.laptop.entity.BeadingLaptop;
import com.spring.jwt.laptop.service.LaptopBeadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beadingLaptops")
@RequiredArgsConstructor
public class LaptopBeadingController {

//  private final LaptopRepository laptopRepo;
    private final LaptopBeadingService laptopBeadingService;

    @PostMapping("/createAuction")
    public BeadingLaptop createAuction(@RequestBody BeadingLaptopDTO dto){
        return laptopBeadingService.addLaptopForAuction(dto);
    }

    @GetMapping("/getAuction/{beadingLaptopId}")
    public BeadingLaptopWithInsDTO getAuction(@PathVariable Long beadingLaptopId) {
        return laptopBeadingService.getLaptopAuctionById(beadingLaptopId);
    }


    @GetMapping("/getAllLiveBiding")
    public List<BeadingLaptopWithInsDTO> live() {
        return laptopBeadingService.getAllLiveLaptopAuctions();
    }

    @PostMapping("/placeBid")
    public BidResponseDTO bid(@RequestBody BidLaptopDTO dto) {
        return laptopBeadingService.placeBid(dto);
    }
}

