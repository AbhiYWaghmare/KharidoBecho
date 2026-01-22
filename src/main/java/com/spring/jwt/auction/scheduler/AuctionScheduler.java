////package com.spring.jwt.auction.scheduler;
////
////import com.spring.jwt.auction.service.AuctionService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.scheduling.annotation.Scheduled;
////import org.springframework.stereotype.Component;
////
////@Component
////@RequiredArgsConstructor
////public class AuctionScheduler {
////
////    private final AuctionService auctionService;
////
////    // every 30 seconds → start auctions and end auctions
////    @Scheduled(fixedDelay = 30000)
////    public void handleAuctionStartAndEnd() {
////        auctionService.startDueAuctions();
////        auctionService.endDueAuctions();
////    }
////
////    // every 1 minute → process expired offers
////    @Scheduled(fixedDelay = 60000)
////    public void handleExpiredOffers() {
////        auctionService.processExpiredOffers();
////    }
////}
//package com.spring.jwt.auction.scheduler;
//
//import com.spring.jwt.auction.service.AuctionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AuctionScheduler {
//
//    private final AuctionService auctionService;
//
//    // every 30 seconds → start auctions and end auctions
//    @Scheduled(fixedDelay = 30000)
//    public void handleAuctionStartAndEnd() {
//        auctionService.startDueAuctions();
//        auctionService.endDueAuctions();
//    }
//
//    // every 1 minute → process expired offers
//    @Scheduled(fixedDelay = 60000)
//    public void handleExpiredOffers() {
//        auctionService.processExpiredOffers();
//    }
//
//
//}
