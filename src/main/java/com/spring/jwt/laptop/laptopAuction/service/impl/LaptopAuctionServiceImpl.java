package com.spring.jwt.laptop.laptopAuction.service.impl;

import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.laptopAuction.dto.LaptopAuctionUpdateMessageDTO;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopAuction;
import com.spring.jwt.laptop.laptopAuction.entity.LaptopBid;
import com.spring.jwt.laptop.laptopAuction.repository.LaptopAuctionRepository;
import com.spring.jwt.laptop.laptopAuction.repository.LaptopBidRepository;
import com.spring.jwt.laptop.laptopAuction.service.LaptopAuctionService;
import com.spring.jwt.laptop.repository.LaptopRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaptopAuctionServiceImpl implements LaptopAuctionService {
    private final LaptopAuctionRepository auctionRepository;
    private final LaptopBidRepository bidRepository;
    private final LaptopRepository laptopRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // =================================================================
    //                      PLACE BID (WebSocket)
    // =================================================================
    @Override
    @Transactional
    public void placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {

        LaptopAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Laptop Auction not found: " + auctionId));

        if (auction.getStatus() != LaptopAuction.AuctionStatus.RUNNING) {
            throw new IllegalStateException("Laptop Auction is not RUNNING");
        }

        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new IllegalStateException("Laptop Auction is already ended");
        }

        BigDecimal minAllowed = auction.getCurrentPrice().add(auction.getMinIncrementInRupees());
        if (bidAmount.compareTo(minAllowed) < 0) {
            throw new IllegalArgumentException("Bid must be at least " + minAllowed);
        }

        LaptopBid bid = LaptopBid.builder()
                .auction(auction)
                .bidderUserId(userId)
                .amount(bidAmount)
                .status(LaptopBid.BidStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        auction.setCurrentPrice(bidAmount);
        auction.setHighestBidderUserId(userId);
        auctionRepository.save(auction);

        // top 3
        List<LaptopBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        List<LaptopAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
                .mapToObj(i -> new LaptopAuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        all.get(i).getBidderUserId(),
                        all.get(i).getAmount()
                ))
                .toList();

        LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                "BID_PLACED",
                auctionId,
                auction.getCurrentPrice(),
                auction.getHighestBidderUserId(),
                top3,
                null
        );

        messagingTemplate.convertAndSend("/topic/laptop-auction/" + auctionId, msg);
    }

    // =================================================================
    //                      START DUE AUCTIONS
    // =================================================================
    @Override
    @Transactional
    public void startDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Laptop startDueAuctions at {}", now);

        List<LaptopAuction> toStart = auctionRepository.findByStatusAndStartTimeLessThanEqual(
                LaptopAuction.AuctionStatus.SCHEDULED, now
        );

        for (LaptopAuction a : toStart) {
            a.setStatus(LaptopAuction.AuctionStatus.RUNNING);
            if (a.getCurrentPrice() == null) {
                a.setCurrentPrice(a.getStartPrice());
            }
            auctionRepository.save(a);

            // Existing message (unchanged)
            LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                    "AUCTION_STARTED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    a.getHighestBidderUserId(),
                    List.of(),
                    null
            );
            messagingTemplate.convertAndSend("/topic/laptop-auction/" + a.getAuctionId(), msg);

            // NEW GLOBAL BROADCAST (required for LIVE auction list)
            messagingTemplate.convertAndSend(
                    "/topic/laptop-auctions/live",
                    new LaptopAuctionUpdateMessageDTO(
                            "LIVE_AUCTION",
                            a.getAuctionId(),
                            a.getCurrentPrice(),
                            a.getHighestBidderUserId(),
                            List.of(),
                            null
                    )
            );
        }
    }


    // =================================================================
    //                      END DUE AUCTIONS
    // =================================================================
    @Override
    @Transactional
    public void endDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Laptop endDueAuctions at {}", now);

        List<LaptopAuction> toEnd = auctionRepository.findExpiredAuctions(
                LaptopAuction.AuctionStatus.RUNNING, now
        );

        for (LaptopAuction a : toEnd) {
            a.setStatus(LaptopAuction.AuctionStatus.ENDED);
            auctionRepository.save(a);

            List<LaptopBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());

            List<LaptopAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
                    .mapToObj(i -> new LaptopAuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            all.get(i).getBidderUserId(),
                            all.get(i).getAmount()
                    ))
                    .toList();

            LocalDateTime offerExpiresAt = null;
            if (!all.isEmpty()) {
                LaptopBid winner = all.get(0);
                winner.setStatus(LaptopBid.BidStatus.WINNING_OFFER);
                offerExpiresAt = LocalDateTime.now().plusHours(24);
                winner.setOfferExpiresAt(offerExpiresAt);
                bidRepository.save(winner);
            }

            // Existing message (unchanged)
            LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                    "AUCTION_ENDED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    a.getHighestBidderUserId(),
                    top3,
                    offerExpiresAt
            );
            messagingTemplate.convertAndSend("/topic/laptop-auction/" + a.getAuctionId(), msg);

            // NEW GLOBAL BROADCAST (required for LIVE auction list)
            messagingTemplate.convertAndSend(
                    "/topic/laptop-auctions/live",
                    new LaptopAuctionUpdateMessageDTO(
                            "AUCTION_ENDED",
                            a.getAuctionId(),
                            a.getCurrentPrice(),
                            a.getHighestBidderUserId(),
                            top3,
                            offerExpiresAt
                    )
            );
        }
    }
    // =================================================================
    //                   PROCESS EXPIRED OFFERS
    // =================================================================
    @Override
    @Transactional
    public void processExpiredOffers() {
        LocalDateTime now = LocalDateTime.now();

        List<LaptopAuction> auctions = auctionRepository.findByStatusAndEndTimeLessThanEqual(
                LaptopAuction.AuctionStatus.ENDED, now
        );

        for (LaptopAuction a : auctions) {
            List<LaptopBid> expired = bidRepository.findExpiredOffers(
                    a, LaptopBid.BidStatus.WINNING_OFFER, now
            );

            if (expired.isEmpty()) continue;

            for (LaptopBid b : expired) {
                b.setStatus(LaptopBid.BidStatus.EXPIRED);
                bidRepository.save(b);
            }

            List<LaptopBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());

            List<LaptopBid> candidates = all.stream()
                    .filter(b -> b.getStatus() == LaptopBid.BidStatus.PLACED)
                    .toList();

            if (candidates.isEmpty()) {
                a.setStatus(LaptopAuction.AuctionStatus.COMPLETED);
                auctionRepository.save(a);

                LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                        "UNSOLD",
                        a.getAuctionId(),
                        a.getCurrentPrice(),
                        a.getHighestBidderUserId(),
                        List.of(),
                        null
                );
                messagingTemplate.convertAndSend("/topic/laptop-auction/" + a.getAuctionId(), msg);

                continue;
            }

            LaptopBid newWinner = candidates.get(0);
            newWinner.setStatus(LaptopBid.BidStatus.WINNING_OFFER);
            LocalDateTime newExpire = LocalDateTime.now().plusHours(24);
            newWinner.setOfferExpiresAt(newExpire);
            bidRepository.save(newWinner);

            List<LaptopAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
                    .mapToObj(i -> new LaptopAuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            all.get(i).getBidderUserId(),
                            all.get(i).getAmount()
                    ))
                    .toList();

            LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                    "WINNER_CHANGED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    newWinner.getBidderUserId(),
                    top3,
                    newExpire
            );

            messagingTemplate.convertAndSend("/topic/laptop-auction/" + a.getAuctionId(), msg);
        }
    }

    // =================================================================
    //                   WINNER ACCEPT
    // =================================================================
    @Override
    @Transactional
    public void winnerAccept(Long auctionId, Long userId) {

        LaptopAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Laptop Auction not found: " + auctionId));

        if (auction.getStatus() != LaptopAuction.AuctionStatus.ENDED) {
            throw new IllegalStateException("Laptop Auction is not in ENDED state");
        }

        List<LaptopBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        LaptopBid winner = all.stream()
                .filter(b -> b.getStatus() == LaptopBid.BidStatus.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not the current laptop winner");
        }

        winner.setStatus(LaptopBid.BidStatus.ACCEPTED);
        bidRepository.save(winner);

        auction.setStatus(LaptopAuction.AuctionStatus.COMPLETED);
        auctionRepository.save(auction);

        Laptop laptop = laptopRepository.findById(auction.getLaptopId())
                .orElseThrow(() -> new IllegalArgumentException("Laptop not found: " + auction.getLaptopId()));
        laptop.setStatus(Status.SOLD);
        laptopRepository.save(laptop);

        LaptopAuctionUpdateMessageDTO msg = new LaptopAuctionUpdateMessageDTO(
                "WINNER_ACCEPTED",
                auctionId,
                auction.getCurrentPrice(),
                userId,
                List.of(),
                null
        );

        messagingTemplate.convertAndSend("/topic/laptop-auction/" + auctionId, msg);
    }

    // =================================================================
    //                   WINNER REJECT
    // =================================================================
    @Override
    @Transactional
    public void winnerReject(Long auctionId, Long userId) {

        LaptopAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Laptop Auction not found: " + auctionId));

        if (auction.getStatus() != LaptopAuction.AuctionStatus.ENDED) {
            throw new IllegalStateException("Laptop Auction is not in ENDED state");
        }

        List<LaptopBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        LaptopBid winner = all.stream()
                .filter(b -> b.getStatus() == LaptopBid.BidStatus.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not current laptop winning bidder");
        }

        winner.setStatus(LaptopBid.BidStatus.REJECTED);
        winner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1)); // expire immediately
        bidRepository.save(winner);

        processExpiredOffers(); // fallback to next candidate
    }
}
