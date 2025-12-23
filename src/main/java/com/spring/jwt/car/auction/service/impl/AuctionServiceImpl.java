package com.spring.jwt.car.auction.service.impl;

import com.spring.jwt.car.auction.dto.AuctionDTO;
import com.spring.jwt.car.auction.dto.AuctionUpdateMessageDTO;
import com.spring.jwt.car.auction.entity.Auction;
import com.spring.jwt.car.auction.entity.Bid;
import com.spring.jwt.car.auction.exception.AuctionNotFoundException;
import com.spring.jwt.car.auction.exception.InvalidAuctionStateException;
import com.spring.jwt.car.auction.exception.InvalidBidException;
import com.spring.jwt.car.auction.mapper.AuctionMapper;
import com.spring.jwt.car.auction.repository.AuctionRepository;
import com.spring.jwt.car.auction.repository.BidRepository;
import com.spring.jwt.car.auction.service.AuctionService;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.repository.CarRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final CarRepository carRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // ===================================================================
    // CREATE AUCTION
    // ===================================================================
    @Override
    @Transactional
    public AuctionDTO createAuction(AuctionDTO dto) {

        Long carId = dto.carId();
        if (carId == null) {
            throw new InvalidAuctionStateException("carId is required");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        Auction auction = new Auction();
        auction.setCar(car);
        auction.setStartPrice(dto.startPrice());
        auction.setCurrentPrice(dto.startPrice());
        auction.setMinIncrementInRupees(dto.minIncrementInRupees());
        auction.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
        auction.setEndTime(dto.endTime());
        auction.setStatus(Auction.Status.SCHEDULED);
        auction.setHighestBidderUserId(null);

        Auction saved = auctionRepository.save(auction);
        return AuctionMapper.toDTO(saved);
    }

    // ===================================================================
    // PLACE BID (WebSocket)
    // ===================================================================
    @Override
    @Transactional
    public AuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {

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

        // Save bid
        Bid bid = Bid.builder()
                .auction(auction)
                .bidderUserId(userId)
                .amount(bidAmount)
                .status(Bid.Status.PLACED)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        // Update auction
        auction.setCurrentPrice(bidAmount);
        auction.setHighestBidderUserId(userId);
        auctionRepository.save(auction);

        // Top 3 bids
        List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        allBids.get(i).getBidderUserId(),
                        allBids.get(i).getAmount()
                ))
                .toList();

        // Create WS message
        AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                "BID_PLACED",
                auctionId,
                auction.getCurrentPrice(),
                auction.getHighestBidderUserId(),
                top3,
                null
        );

        // Send WS update
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, msg);

        // 🔥 THIS FIXES THE ERROR
        return msg;
    }


    // ===================================================================
    // START DUE AUCTIONS
    // ===================================================================
    @Override
    @Transactional
    public void startDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> toStart = auctionRepository.findByStatusAndStartTimeLessThanEqual(
                Auction.Status.SCHEDULED, now);

        for (Auction auction : toStart) {
            auction.setStatus(Auction.Status.RUNNING);
            if (auction.getCurrentPrice() == null) {
                auction.setCurrentPrice(auction.getStartPrice());
            }
            auctionRepository.save(auction);

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "AUCTION_STARTED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    auction.getHighestBidderUserId(),
                    List.of(),
                    null
            );

            messagingTemplate.convertAndSend("/topic/auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // END AUCTIONS
    // ===================================================================
    @Override
    @Transactional
    public void endDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> toEnd = auctionRepository.findExpiredAuctions(Auction.Status.RUNNING, now);

        for (Auction auction : toEnd) {

            auction.setStatus(Auction.Status.ENDED);
            auctionRepository.save(auction);

            List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auction.getAuctionId());

            // Mark highest bid as WINNING OFFER
            LocalDateTime offerExpiresAt = null;

            if (!allBids.isEmpty()) {
                Bid winner = allBids.get(0);
                winner.setStatus(Bid.Status.WINNING_OFFER);
                offerExpiresAt = LocalDateTime.now().plusHours(24);
                winner.setOfferExpiresAt(offerExpiresAt);
                bidRepository.save(winner);
            }

            List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "AUCTION_ENDED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    auction.getHighestBidderUserId(),
                    top3,
                    offerExpiresAt
            );

            messagingTemplate.convertAndSend("/topic/auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // PROCESS EXPIRED OFFERS
    // ===================================================================
    @Override
    @Transactional
    public void processExpiredOffers() {
        LocalDateTime now = LocalDateTime.now();

        List<Auction> endedAuctions = auctionRepository.findByStatusAndEndTimeLessThanEqual(
                Auction.Status.ENDED, now);

        for (Auction auction : endedAuctions) {

            List<Bid> expiredWinners = bidRepository.findExpiredOffers(
                    auction, Bid.Status.WINNING_OFFER, now);

            if (expiredWinners.isEmpty()) continue;

            expiredWinners.forEach(b -> {
                b.setStatus(Bid.Status.EXPIRED);
                bidRepository.save(b);
            });

            // find next highest
            List<Bid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auction.getAuctionId());

            List<Bid> candidates = allBids.stream()
                    .filter(b -> b.getStatus() == Bid.Status.PLACED)
                    .toList();

            if (candidates.isEmpty()) {
                auction.setStatus(Auction.Status.COMPLETED);
                auctionRepository.save(auction);

                AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                        "UNSOLD",
                        auction.getAuctionId(),
                        auction.getCurrentPrice(),
                        auction.getHighestBidderUserId(),
                        List.of(),
                        null
                );

                messagingTemplate.convertAndSend("/topic/auction/" + auction.getAuctionId(), msg);
                continue;
            }

            Bid newWinner = candidates.get(0);
            LocalDateTime newExpire = LocalDateTime.now().plusHours(24);

            newWinner.setStatus(Bid.Status.WINNING_OFFER);
            newWinner.setOfferExpiresAt(newExpire);
            bidRepository.save(newWinner);

            List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            AuctionUpdateMessageDTO msg = new AuctionUpdateMessageDTO(
                    "WINNER_CHANGED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    newWinner.getBidderUserId(),
                    top3,
                    newExpire
            );

            messagingTemplate.convertAndSend("/topic/auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // WINNER ACCEPT
    // ===================================================================
    @Override
    @Transactional
    public void winnerAccept(Long auctionId, Long userId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        if (auction.getStatus() != Auction.Status.ENDED) {
            throw new InvalidAuctionStateException("Auction is not ENDED");
        }

        List<Bid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        Bid winner = all.stream()
                .filter(b -> b.getStatus() == Bid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new InvalidAuctionStateException("No winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("Not allowed");
        }

        winner.setStatus(Bid.Status.ACCEPTED);
        bidRepository.save(winner);

        auction.setStatus(Auction.Status.COMPLETED);
        auctionRepository.save(auction);

        Car car = auction.getCar();
        car.setStatus(Car.Status.SOLD);
        carRepository.save(car);

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
//    ++++++++++++++++++++++++++++++
    @Override
    public Auction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found with id " + auctionId));
    }


    // ===================================================================
    // WINNER REJECT
    // ===================================================================
    @Override
    @Transactional
    public void winnerReject(Long auctionId, Long userId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        if (auction.getStatus() != Auction.Status.ENDED) {
            throw new InvalidAuctionStateException("Auction is not ENDED");
        }

        List<Bid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        // Current winner
        Bid winner = all.stream()
                .filter(b -> b.getStatus() == Bid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new InvalidAuctionStateException("No winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("Not allowed");
        }

        // Reject current winner
        winner.setStatus(Bid.Status.REJECTED);
        winner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1));
        bidRepository.save(winner);

        // -----------------------------------------------------
        // 🔥 Select next highest bidder immediately
        // -----------------------------------------------------

        List<Bid> nextCandidates = all.stream()
                .filter(b -> b.getStatus() == Bid.Status.PLACED)
                .toList();

        if (nextCandidates.isEmpty()) {
            // No bidders left → mark UNSOLD
            auction.setStatus(Auction.Status.COMPLETED);
            auction.setHighestBidderUserId(null);
            auctionRepository.save(auction);

            messagingTemplate.convertAndSend(
                    "/topic/auction/" + auctionId,
                    new AuctionUpdateMessageDTO("UNSOLD", auctionId, auction.getCurrentPrice(), null, List.of(), null)
            );
            return;
        }

        // Next highest bidder
        Bid next = nextCandidates.get(0);
        next.setStatus(Bid.Status.WINNING_OFFER);
        next.setOfferExpiresAt(LocalDateTime.now().plusHours(24));
        bidRepository.save(next);

        // -----------------------------------------------------
        // 🔥 Update Auction table immediately
        // -----------------------------------------------------
        auction.setHighestBidderUserId(next.getBidderUserId());
        auction.setCurrentPrice(next.getAmount());
        auctionRepository.save(auction);

        // Build Top 3 list
        List<AuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
                .mapToObj(i -> new AuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        all.get(i).getBidderUserId(),
                        all.get(i).getAmount()
                ))
                .toList();

        // -----------------------------------------------------
        // 🔥 Send WS update to frontend
        // -----------------------------------------------------
        messagingTemplate.convertAndSend(
                "/topic/auction/" + auctionId,
                new AuctionUpdateMessageDTO(
                        "WINNER_CHANGED",
                        auctionId,
                        auction.getCurrentPrice(),
                        next.getBidderUserId(),
                        top3,
                        next.getOfferExpiresAt()
                )
        );

    }

}
