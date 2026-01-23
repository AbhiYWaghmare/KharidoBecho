//package com.spring.jwt.car.auction.scheduler;
//
//import com.spring.jwt.car.auction.service.CarAuctionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CarAuctionScheduler {
//
//    private final CarAuctionService carauctionService;
//
//    // 🔁 Every 30 seconds → start & end auctions
//    @Scheduled(fixedDelay = 30000)
//    public void handleStartAndEnd() {
//        log.info("🚗 Car Auction scheduler fired");
//
//        carauctionService.startDueAuctions();
//        carauctionService.endDueAuctions();
//
//        carauctionService.broadcastRunningAuctions();
//    }
//
//    // 🔁 Every 60 seconds → expire offers
//    @Scheduled(fixedDelay = 60000)
//    public void handleExpiredOffers() {
//        log.info("⏰ Car Auction expired offers check");
//
//        carauctionService.processExpiredOffers();
//    }
//}
/////