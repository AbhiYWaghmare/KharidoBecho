package com.spring.jwt.laptop.laptopAuction.scheduler;

import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class LaptopAuctionScheduler {
    private final LaptopAuctionService auctionService;

    @Scheduled(fixedDelay = 30000)
    public void handleStartAndEnd() {
        auctionService.startDueAuctions();
        auctionService.endDueAuctions();
    }

    @Scheduled(fixedDelay = 60000)
    public void handleExpiredOffers() {
        auctionService.processExpiredOffers();
    }
}
