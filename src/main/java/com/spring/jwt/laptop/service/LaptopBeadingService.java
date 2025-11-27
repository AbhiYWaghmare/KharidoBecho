package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.dto.BeadingLaptopDTO;
import com.spring.jwt.laptop.dto.BeadingLaptopWithInsDTO;
import com.spring.jwt.laptop.dto.BidLaptopDTO;
import com.spring.jwt.laptop.dto.BidResponseDTO;
import com.spring.jwt.laptop.entity.BeadingLaptop;
import com.spring.jwt.laptop.repository.BeadingLaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface LaptopBeadingService {

        BeadingLaptop addLaptopForAuction(BeadingLaptopDTO dto);

        BeadingLaptopWithInsDTO getLaptopAuctionById(Long beadingLaptopId);

        List<BeadingLaptopWithInsDTO> getAllLiveLaptopAuctions();

        List<BeadingLaptopWithInsDTO> getLaptopAuctionsBySeller(Long sellerId);

        BidResponseDTO placeBid(BidLaptopDTO bidDto);
}
