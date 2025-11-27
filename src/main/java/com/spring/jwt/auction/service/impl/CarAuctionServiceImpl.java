package com.spring.jwt.auction.service.impl;

import com.spring.jwt.auction.entity.CarAuction;
import com.spring.jwt.auction.repository.CarAuctionRepository;
import com.spring.jwt.auction.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class CarAuctionServiceImpl implements AuctionService {

    @Autowired
    private CarAuctionRepository carAuctionRepository;

    @Override
    public void placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {

    }

    // ========================= START AUCTIONS =========================
    @Override
    @Scheduled(cron = "0/30 * * * * ?")  // Runs at 00 sec and 30 sec
    @Transactional
    public void startDueAuctions() {

        OffsetDateTime now = OffsetDateTime.now();
        log.info("🔵 startDueAuctions triggered at {}", now);

        List<CarAuction> dueAuctions = carAuctionRepository
                .findByStatusAndStartTimeLessThanEqual(
                        CarAuction.Status.SCHEDULED,
                        now
                );

        if (dueAuctions.isEmpty()) {
            log.info("No auctions to start.");
            return;
        }

        for (CarAuction auction : dueAuctions) {
            try {
                auction.setStatus(CarAuction.Status.ACTIVE);

                if (auction.getCurrentPrice() == null) {
                    auction.setCurrentPrice(auction.getStartPrice());
                }

                log.info("Auction STARTED → ID: {}, StartPrice: {}",
                        auction.getAuctionId(), auction.getStartPrice());

            } catch (Exception e) {
                log.error("Error starting auction ID {}: {}", auction.getAuctionId(), e.getMessage());
            }
        }
    }


    // ========================= END AUCTIONS =========================
    @Override
    @Scheduled(cron = "15/30 * * * * ?")  // Runs at 15 sec and 45 sec
    @Transactional
    public void endDueAuctions() {

        OffsetDateTime now = OffsetDateTime.now();
        log.info("🔴 endDueAuctions triggered at {}", now);

        // 🔥 FIX: endTime वापरायचा होता, startTime नाही
        List<CarAuction> dueEndAuctions =
                carAuctionRepository.findByStatusAndEndTimeLessThanEqual(
                        CarAuction.Status.ACTIVE,
                        now
                );

        if (dueEndAuctions.isEmpty()) {
            log.info("No auctions to end.");
            return;
        }

        for (CarAuction auction : dueEndAuctions) {
            try {
                auction.setStatus(CarAuction.Status.ENDED);

                log.info("Auction ENDED → ID: {}, FinalPrice: {}, WinnerUserId: {}",
                        auction.getAuctionId(),
                        auction.getCurrentPrice(),
                        auction.getHighestBidderUserId());

            } catch (Exception e) {
                log.error("Error ending auction ID {}: {}", auction.getAuctionId(), e.getMessage());
            }
        }
    }


    @Override
    public void processExpiredOffers() {

    }

    @Override
    public void winnerAccept(Long auctionId, Long userId) {

    }

    @Override
    public void winnerReject(Long auctionId, Long userId) {

    }
}
