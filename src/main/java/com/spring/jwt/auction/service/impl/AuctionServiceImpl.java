package com.spring.jwt.auction.service.impl;

import com.spring.jwt.auction.dto.AuctionDTO;
import com.spring.jwt.auction.dto.AuctionRequestDTO;
import com.spring.jwt.auction.dto.AuctionUpdateMessageDTO;
import com.spring.jwt.auction.entity.Auction;
import com.spring.jwt.auction.entity.Bid;
import com.spring.jwt.auction.exception.AuctionNotFoundException;
import com.spring.jwt.auction.exception.InvalidAuctionStateException;
import com.spring.jwt.auction.exception.InvalidBidException;
import com.spring.jwt.auction.mapper.AuctionMapper;
import com.spring.jwt.auction.repository.AuctionRepository;
import com.spring.jwt.auction.repository.BidRepository;
import com.spring.jwt.auction.service.AuctionService;
import com.spring.jwt.Mobile.Repository.MobileRepository;
import com.spring.jwt.Mobile.entity.Mobile;
import com.spring.jwt.exception.mobile.MobileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final MobileRepository mobileRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // =============== BIDDING (called by WebSocket) ==================

    @Override
    @Transactional

    public AuctionDTO createAuction(AuctionRequestDTO dto) {
        Long mobileId = dto.mobileId();

        if (mobileId == null) {
            throw new InvalidAuctionStateException("mobileId is required");
        }

        Mobile mobile = mobileRepository.findById(mobileId)
                .orElseThrow(() -> new MobileNotFoundException(mobileId));


        Auction a = new Auction();
        a.setMobile(mobile);
        a.setStartPrice(dto.startPrice());
        a.setCurrentPrice(dto.startPrice());
        a.setMinIncrementInRupees(dto.minIncrementInRupees());
        a.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
        a.setEndTime(dto.endTime());
        a.setStatus(Auction.Status.SCHEDULED);
        a.setHighestBidderUserId(null);

        Auction saved = auctionRepository.save(a);
        return AuctionMapper.toDTO(saved);
    }



    public void placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {
        // load auction
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + auctionId));

        if (auction.getStatus() != Auction.Status.RUNNING) {
            throw new InvalidAuctionStateException("Auction is not RUNNING");
        }

        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new InvalidAuctionStateException("Auction already ended");
        }

        BigDecimal minAllowed = auction.getCurrentPrice().add(auction.getMinIncrementInRupees());
        if (bidAmount.compareTo(minAllowed) < 0) {
            throw new InvalidBidException("Bid must be at least " + minAllowed);
        }

        // create bid
        Bid bid = Bid.builder()
                .auction(auction)
                .bidderUserId(userId)
                .amount(bidAmount)
                .status(Bid.Status.PLACED)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        // update auction
        auction.setCurrentPrice(bidAmount);
        auction.setHighestBidderUserId(userId);
        auctionRepository.save(auction);

        // rebuild top 3
        List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        allBids.get(i).getBidderUserId(),
                        allBids.get(i).getAmount()
                ))
                .toList();

        AuctionUpdateMessageDTO message = new AuctionUpdateMessageDTO(
                "BID_PLACED",
                auctionId,
                auction.getCurrentPrice(),
                auction.getHighestBidderUserId(),
                top3,
                null
        );

        // broadcast to /topic/auction/{auctionId}
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, message);
    }

    // ============ START / END SCHEDULING ============================

    @Override
    @Transactional
    public void startDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("startDueAuctions at {}", now);
        List<Auction> toStart = auctionRepository.findByStatusAndStartTimeLessThanEqual(
                Auction.Status.SCHEDULED, now);

        for (Auction a : toStart) {
            a.setStatus(Auction.Status.RUNNING);
            // ensure currentPrice initialized
            if (a.getCurrentPrice() == null) {
                a.setCurrentPrice(a.getStartPrice());
            }
            auctionRepository.save(a);

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "AUCTION_STARTED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    a.getHighestBidderUserId(),
                    List.of(),
                    null
            );
            messagingTemplate.convertAndSend("/topic/auction/" + a.getAuctionId(), msg);
        }
    }

    @Override
    @Transactional
    public void endDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("endDueAuctions at {}", now);
        List<Auction> toEnd = auctionRepository.findExpiredAuctions(Auction.Status.RUNNING, now);

        for (Auction a : toEnd) {
            a.setStatus(Auction.Status.ENDED);
            auctionRepository.save(a);

            // determine top3
            List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());
            List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            // mark highest as WINNING_OFFER (if exists)
            LocalDateTime offerExpiresAt = null;
            if (!allBids.isEmpty()) {
                Bid winner = allBids.get(0);
                winner.setStatus(Bid.Status.WINNING_OFFER);
                offerExpiresAt = LocalDateTime.now().plusHours(24); // e.g. 24 hours
                winner.setOfferExpiresAt(offerExpiresAt);
                bidRepository.save(winner);
            }

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "AUCTION_ENDED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    a.getHighestBidderUserId(),
                    top3,
                    offerExpiresAt
            );
            messagingTemplate.convertAndSend("/topic/auction/" + a.getAuctionId(), msg);
        }
    }

    // ============ PROCESS EXPIRED OFFERS (top3 fallback) ============

    @Override
    @Transactional
    public void processExpiredOffers() {
        LocalDateTime now = LocalDateTime.now();

        // find auctions which are ENDED and have some WINNING_OFFER that expired.
        List<Auction> endedAuctions = auctionRepository.findByStatusAndEndTimeLessThanEqual(
                Auction.Status.ENDED, now);

        for (Auction a : endedAuctions) {
            // expired current winning offer
            List<Bid> expiredWinners = bidRepository.findExpiredOffers(
                    a, Bid.Status.WINNING_OFFER, now);

            if (expiredWinners.isEmpty()) continue;

            // mark the existing winning offers as EXPIRED
            for (Bid b : expiredWinners) {
                b.setStatus(Bid.Status.EXPIRED);
                bidRepository.save(b);
            }

            // find top bids again
            List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());

            // skip ones that are REJECTED/EXPIRED/ACCEPTED
            List<Bid> candidates = allBids.stream()
                    .filter(b -> b.getStatus() == Bid.Status.PLACED)
                    .toList();

            if (candidates.isEmpty()) {
                // nobody left → mark auction COMPLETED unsold
                a.setStatus(Auction.Status.COMPLETED);
                auctionRepository.save(a);

                AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                        "UNSOLD",
                        a.getAuctionId(),
                        a.getCurrentPrice(),
                        a.getHighestBidderUserId(),
                        List.of(),
                        null
                );
                messagingTemplate.convertAndSend("/topic/auction/" + a.getAuctionId(), msg);

                continue;
            }

            // next highest candidate becomes WINNING_OFFER
            Bid newWinner = candidates.get(0);
            newWinner.setStatus(Bid.Status.WINNING_OFFER);
            LocalDateTime newExpire = LocalDateTime.now().plusHours(24);
            newWinner.setOfferExpiresAt(newExpire);
            bidRepository.save(newWinner);

            // top3 again
            List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "WINNER_CHANGED",
                    a.getAuctionId(),
                    a.getCurrentPrice(),
                    newWinner.getBidderUserId(),
                    top3,
                    newExpire
            );
            messagingTemplate.convertAndSend("/topic/auction/" + a.getAuctionId(), msg);
        }
    }

    // ============ WINNER ACCEPT / REJECT ============================

    @Override
    @Transactional
    public void winnerAccept(Long auctionId, Long userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + auctionId));

        if (auction.getStatus() != Auction.Status.ENDED) {
            throw new InvalidAuctionStateException("Auction is not in ENDED state");
        }

        // find current winning offer
        List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        Bid winner = allBids.stream()
                .filter(b -> b.getStatus() == Bid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new InvalidAuctionStateException("No active winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("This user is not the current winner");
        }

        winner.setStatus(Bid.Status.ACCEPTED);
        bidRepository.save(winner);

        // mark auction as completed & mark mobile SOLD
        auction.setStatus(Auction.Status.COMPLETED);
        auctionRepository.save(auction);

        Mobile mobile = auction.getMobile();
        if (mobile == null) {
            throw new MobileNotFoundException("Auction has no associated mobile");
        }

        mobile.setStatus(Mobile.Status.SOLD);
        mobileRepository.save(mobile);


        AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                "WINNER_ACCEPTED",
                auctionId,
                auction.getCurrentPrice(),
                userId,
                List.of(),
                null
        );
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, msg);
    }

    @Override
    @Transactional
    public void winnerReject(Long auctionId, Long userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + auctionId));

        if (auction.getStatus() != Auction.Status.ENDED) {
            throw new InvalidAuctionStateException("Auction is not in ENDED state");
        }

        List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        Bid winner = allBids.stream()
                .filter(b -> b.getStatus() == Bid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new InvalidAuctionStateException("No active winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("This user is not the current winner");
        }

        winner.setStatus(Bid.Status.REJECTED);
        bidRepository.save(winner);

        // now treat same as expired offer: try second highest
        // you can just call processExpiredOffers() for this auction logic,
        // but for simplicity call the logic inline or reuse.

        // easiest: mark offerExpiresAt < now and call processExpiredOffers() in single-thread.
        winner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1));
        bidRepository.save(winner);
        processExpiredOffers();  // will pick the next candidate
    }
}