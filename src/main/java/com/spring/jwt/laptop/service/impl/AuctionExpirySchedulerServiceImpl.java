package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.laptop.entity.BeadingLaptop;
import com.spring.jwt.laptop.model.AuctionStatus;
import com.spring.jwt.laptop.repository.BeadingLaptopRepository;
import com.spring.jwt.laptop.service.AuctionExpirySchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionExpirySchedulerServiceImpl implements AuctionExpirySchedulerService {
    private final BeadingLaptopRepository beadingLaptopRepo;

    // runs every 1 minute
    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {

        LocalDateTime now = LocalDateTime.now();

        // fetch ACTIVE auctions only
        List<BeadingLaptop> activeAuctions = beadingLaptopRepo.findByAuctionStatus(AuctionStatus.LIVE);

        for (BeadingLaptop auction : activeAuctions) {
            if (auction.getClosingTime().isBefore(now)) {

                auction.setAuctionStatus(AuctionStatus.CLOSED);

                beadingLaptopRepo.save(auction);

                System.out.println("Auction closed: " + auction.getBeadingLaptopId());
            }
        }
    }
}
