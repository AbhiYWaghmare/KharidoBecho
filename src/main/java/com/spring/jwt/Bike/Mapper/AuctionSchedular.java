package com.spring.jwt.Bike.Mapper;

import com.spring.jwt.Bike.Entity.BikeBidding;
import com.spring.jwt.Bike.Repository.WebsocketRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component
@EnableScheduling
public class AuctionSchedular {

    public AuctionSchedular(WebsocketRepository websocketRepository) {
        this.websocketRepository = websocketRepository;
    }

    private final WebsocketRepository websocketRepository;


    @Scheduled(fixedRate = 100000)  // every 100 seconds
    public void autoCloseAuctions() {

        List<BikeBidding> list = websocketRepository.findAll();

        for (BikeBidding bidding : list) {

            if (bidding.isActive() && LocalDateTime.now().isAfter(bidding.getClosingAt())) {

                bidding.setActive(false);
                websocketRepository.save(bidding);

                System.out.println("AUTO CLOSED AUCTION ID: " + bidding.getBiddingId());
            }
        }
    }

}
