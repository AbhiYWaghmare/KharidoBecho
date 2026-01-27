package com.spring.jwt.socketio.scheduler;

import com.spring.jwt.socketio.entity.LaptopAuction;
import com.spring.jwt.socketio.repository.LaptopAuctionRepository;
import com.spring.jwt.socketio.service.SocketIOService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LaptopAuctionScheduler {

    private final LaptopAuctionRepository auctionRepo;
    private final SocketIOService socketIOService;

    @Value("${auction.scheduler.enabled:true}")
    private boolean schedulerEnabled;


    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void updateAuctionStates() {

        if (!schedulerEnabled) return;

        LocalDateTime now = LocalDateTime.now();

        // SCHEDULED → RUNNING
        var toStart =
                auctionRepo.findByStatusAndStartTimeLessThanEqual(
                        LaptopAuction.AuctionStatus.SCHEDULED, now);

        for (LaptopAuction a : toStart) {
            if (a.getStatus() != LaptopAuction.AuctionStatus.RUNNING) {
                a.setStatus(LaptopAuction.AuctionStatus.RUNNING);
            }
            auctionRepo.save(a);
            socketIOService.sendToAll(
                    "auctionStarted",
                    Map.of("auctionId", a.getAuctionId())
            );

        }

        // RUNNING → ENDED
        var toEnd =
                auctionRepo.findByStatusAndEndTimeLessThanEqual(
                        LaptopAuction.AuctionStatus.RUNNING, now);

        for (LaptopAuction a : toEnd) {
            if (a.getStatus() != LaptopAuction.AuctionStatus.ENDED) {
                a.setStatus(LaptopAuction.AuctionStatus.ENDED);
            }
            auctionRepo.save(a);
            socketIOService.sendToAll(
                    "auctionEnded",
                    Map.of("auctionId", a.getAuctionId())
            );
        }
    }
}
