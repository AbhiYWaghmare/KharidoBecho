package com.spring.jwt.Bike.Websocket.Schedular;

import com.spring.jwt.Bike.Websocket.Service.BikeAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BikeAuctionSchedular {

    private final BikeAuctionService auctionService;

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
