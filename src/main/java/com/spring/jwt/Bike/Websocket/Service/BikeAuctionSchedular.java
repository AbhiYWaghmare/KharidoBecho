//package com.spring.jwt.Bike.Websocket.Service;
//
//import com.spring.jwt.Bike.Websocket.Entity.BikeBidd;
//import com.spring.jwt.Bike.Websocket.Repository.BikeBiddRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BikeAuctionSchedular {
//
//
//    private final BikeBiddRepository bikeBiddRepository;
//
//    @Scheduled(fixedRate = 30000) // every 30 seconds
//    public void closeExpiredAuctions() {
//
//        LocalDateTime now = LocalDateTime.now();
//
//        List<BikeBidd> expiredAuctions =
//                bikeBiddRepository.findByActiveTrueAndClosingAtBefore(now);
//
//        for (BikeBidd auction : expiredAuctions) {
//            auction.setActive(false);
//
//            //set winner, send notification, etc.
//        }
//
//        bikeBiddRepository.saveAll(expiredAuctions);
//    }
//}
//
//
