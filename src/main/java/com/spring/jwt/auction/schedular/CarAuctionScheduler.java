package com.spring.jwt.auction.schedular;

import com.spring.jwt.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarAuctionScheduler {

    private final AuctionService carAuctionService;

    @Scheduled(fixedDelay = 30000)
    public void handleAuctionStartAndEnd() {
        carAuctionService.startDueAuctions();
        carAuctionService.endDueAuctions();
    }

    @Scheduled(fixedDelay = 60000)
    public void handleExpiredOffers() {
        carAuctionService.processExpiredOffers();
    }
}
