package com.spring.jwt.auction.scheduler;

import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.entity.Bid;
import com.spring.jwt.auction.entity.BidStatus;
import com.spring.jwt.auction.repo.AuctionRepository;
import com.spring.jwt.auction.repo.BidRepository;
import com.spring.jwt.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepo;
    private final BidRepository bidRepo;
    private final AuctionService auctionService;

    // runs every 15 seconds – tune as needed
    @Scheduled(fixedDelayString = "${auction.scheduler.delay:15000}")
    public void checkAuctions() {
        // 1) finalize ended live auctions
        List<Auction> toFinalize = auctionRepo.findByStatusAndEndTimeBefore(com.spring.jwt.auction.entity.AuctionStatus.LIVE, OffsetDateTime.now());
        for (Auction a : toFinalize) {
            try {
                auctionService.finalizeAuction(a.getAuctionId());
            } catch (Exception e) {
                // log and continue
            }
        }

        // 2) find expired pending offers and promote next
        List<Bid> expiredOffers = bidRepo.findByStatusAndOfferExpiresAtBefore(BidStatus.PENDING_CONFIRMATION, OffsetDateTime.now());
        for (Bid b : expiredOffers) {
            try {
                auctionService.rejectOfferAndPromoteNext(b);
            } catch (Exception e) {
                // log and continue
            }
        }
    }
}
