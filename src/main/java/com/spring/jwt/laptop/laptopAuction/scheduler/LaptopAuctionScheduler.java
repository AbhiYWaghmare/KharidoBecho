//package com.spring.jwt.laptop.laptopAuction.scheduler;
//
//import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class LaptopAuctionScheduler {
//    private final LaptopAuctionService auctionService;
//
//    @Scheduled(fixedDelay = 30000)
//    public void handleStartAndEnd() {
//        log.info("Auction scheduler fired");
//        auctionService.startDueAuctions();
//        auctionService.endDueAuctions();
//
//        auctionService.broadcastRunningAuctions();
//
//    }
//
//    @Scheduled(fixedDelay = 60000)
//    public void handleExpiredOffers() {
//        auctionService.processExpiredOffers();
//    }
//
//}
