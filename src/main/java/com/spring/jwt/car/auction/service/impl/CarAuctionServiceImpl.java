package com.spring.jwt.car.auction.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.car.auction.dto.CarAuctionDTO;
import com.spring.jwt.car.auction.dto.CarAuctionUpdateMessageDTO;
import com.spring.jwt.car.auction.entity.CarAuction;
import com.spring.jwt.car.auction.entity.CarBid;
import com.spring.jwt.car.auction.exception.CarAuctionNotFoundException;
import com.spring.jwt.car.auction.exception.CarInvalidAuctionStateException;
import com.spring.jwt.car.auction.exception.InvalidBidException;
import com.spring.jwt.car.auction.mapper.CarAuctionMapper;
import com.spring.jwt.car.auction.repository.CarAuctionRepository;
import com.spring.jwt.car.auction.repository.CarBidRepository;
import com.spring.jwt.car.auction.service.CarAuctionService;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.repository.CarBookingRepository;
import com.spring.jwt.car.repository.CarRepository;

import com.spring.jwt.car.auction.carsocket.dto.CarChatMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarAuctionServiceImpl implements CarAuctionService {

    private final CarAuctionRepository auctionRepository;
    private final CarBidRepository bidRepository;
    private final CarRepository carRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CarBookingRepository carBookingRepository;

    // ===================================================================
    // CREATE AUCTION
    // ===================================================================
    @Override
    @Transactional
    public CarAuctionDTO createAuction(CarAuctionDTO dto) {

        Long carId = dto.carId();
        if (carId == null) {
            throw new CarInvalidAuctionStateException("carId is required");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        CarAuction auction = new CarAuction();
        auction.setCar(car);
        auction.setStartPrice(dto.startPrice());
        auction.setCurrentPrice(dto.startPrice());
        auction.setMinIncrementInRupees(dto.minIncrementInRupees());
        auction.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
        auction.setEndTime(dto.endTime());
        auction.setStatus(CarAuction.Status.SCHEDULED);
        auction.setHighestBidderUserId(null);

        CarAuction saved = auctionRepository.save(auction);
        return CarAuctionMapper.toDTO(saved);
    }

    // ===================================================================
    // PLACE BID (WebSocket)
    // ===================================================================
    @Override
    @Transactional
    public CarAuctionUpdateMessageDTO placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {

        CarAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CarAuctionNotFoundException(auctionId));

        if (auction.getStatus() != CarAuction.Status.RUNNING) {
            throw new CarInvalidAuctionStateException("Auction is not RUNNING");
        }

        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new CarInvalidAuctionStateException("Auction already ended");
        }

        BigDecimal minAllowed = auction.getCurrentPrice().add(auction.getMinIncrementInRupees());
        if (bidAmount.compareTo(minAllowed) < 0) {
            throw new InvalidBidException("Bid must be at least " + minAllowed);
        }

        // Save bid
        CarBid bid = CarBid.builder()
                .auction(auction)
                .bidderUserId(userId)
                .amount(bidAmount)
                .status(CarBid.Status.PLACED)
                .createdAt(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        // Update auction
        auction.setCurrentPrice(bidAmount);
        auction.setHighestBidderUserId(userId);
        auctionRepository.save(auction);

        // Top 3 bids
        List<CarBid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
        List<CarAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                .mapToObj(i -> new CarAuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        allBids.get(i).getBidderUserId(),
                        allBids.get(i).getAmount()
                ))
                .toList();

        // Create WS message
        CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                "BID_PLACED",
                auctionId,
                auction.getCurrentPrice(),
                auction.getHighestBidderUserId(),
                top3,
                null
        );

        // Send WS update
        messagingTemplate.convertAndSend(
                "/topic/car-auction/" + auctionId,
                msg
        );

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
        List<CarAuction> toStart = auctionRepository.findByStatusAndStartTimeLessThanEqual(
                CarAuction.Status.SCHEDULED, now);

        for (CarAuction auction : toStart) {
            auction.setStatus(CarAuction.Status.RUNNING);
            if (auction.getCurrentPrice() == null) {
                auction.setCurrentPrice(auction.getStartPrice());
            }
            auctionRepository.save(auction);

            CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                    "AUCTION_STARTED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    auction.getHighestBidderUserId(),
                    List.of(),
                    null
            );

            messagingTemplate.convertAndSend("/topic/car-auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // END AUCTIONS
    // ===================================================================
    @Override
    @Transactional
    public void endDueAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<CarAuction> toEnd = auctionRepository.findExpiredAuctions(CarAuction.Status.RUNNING, now);

        for (CarAuction auction : toEnd) {

            auction.setStatus(CarAuction.Status.ENDED);
            auctionRepository.save(auction);

            List<CarBid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auction.getAuctionId());

            // Mark highest bid as WINNING OFFER
            LocalDateTime offerExpiresAt = null;

            if (!allBids.isEmpty()) {
                CarBid winner = allBids.get(0);
                winner.setStatus(CarBid.Status.WINNING_OFFER);
                offerExpiresAt = LocalDateTime.now().plusHours(24);
                winner.setOfferExpiresAt(offerExpiresAt);
                bidRepository.save(winner);
            }

            List<CarAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new CarAuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                    "AUCTION_ENDED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    auction.getHighestBidderUserId(),
                    top3,
                    offerExpiresAt
            );

            messagingTemplate.convertAndSend("/topic/car-auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // PROCESS EXPIRED OFFERS
    // ===================================================================
    @Override
    @Transactional
    public void processExpiredOffers() {
        LocalDateTime now = LocalDateTime.now();

        List<CarAuction> endedAuctions = auctionRepository.findByStatusAndEndTimeLessThanEqual(
                CarAuction.Status.ENDED, now);

        for (CarAuction auction : endedAuctions) {

            List<CarBid> expiredWinners = bidRepository.findExpiredOffers(
                    auction, CarBid.Status.WINNING_OFFER, now);

            if (expiredWinners.isEmpty()) continue;

            expiredWinners.forEach(b -> {
                b.setStatus(CarBid.Status.EXPIRED);
                bidRepository.save(b);
            });

            // find next highest
            List<CarBid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(auction.getAuctionId());

            List<CarBid> candidates = allBids.stream()
                    .filter(b -> b.getStatus() == CarBid.Status.PLACED)
                    .toList();

            if (candidates.isEmpty()) {
                auction.setStatus(CarAuction.Status.COMPLETED);
                auctionRepository.save(auction);

                CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                        "UNSOLD",
                        auction.getAuctionId(),
                        auction.getCurrentPrice(),
                        auction.getHighestBidderUserId(),
                        List.of(),
                        null
                );

                messagingTemplate.convertAndSend("/topic/car-auction/" + auction.getAuctionId(), msg);
                continue;
            }

            CarBid newWinner = candidates.get(0);
            LocalDateTime newExpire = LocalDateTime.now().plusHours(24);

            newWinner.setStatus(CarBid.Status.WINNING_OFFER);
            newWinner.setOfferExpiresAt(newExpire);
            bidRepository.save(newWinner);

            List<CarAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, allBids.size()))
                    .mapToObj(i -> new CarAuctionUpdateMessageDTO.TopBidDTO(
                            i + 1,
                            allBids.get(i).getBidderUserId(),
                            allBids.get(i).getAmount()
                    ))
                    .toList();

            CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                    "WINNER_CHANGED",
                    auction.getAuctionId(),
                    auction.getCurrentPrice(),
                    newWinner.getBidderUserId(),
                    top3,
                    newExpire
            );

            messagingTemplate.convertAndSend("/topic/car-auction/" + auction.getAuctionId(), msg);
        }
    }

    // ===================================================================
    // WINNER ACCEPT
    // ===================================================================
    @Override
    @Transactional
    public void winnerAccept(Long auctionId, Long userId) {

        CarAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CarAuctionNotFoundException(auctionId));

        if (auction.getStatus() != CarAuction.Status.ENDED) {
            throw new CarInvalidAuctionStateException("Auction is not ENDED");
        }

        List<CarBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        CarBid winner = all.stream()
                .filter(b -> b.getStatus() == CarBid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new CarInvalidAuctionStateException("No winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("Not allowed");
        }

        winner.setStatus(CarBid.Status.ACCEPTED);
        bidRepository.save(winner);

        auction.setStatus(CarAuction.Status.COMPLETED);
        auctionRepository.save(auction);

        Car car = auction.getCar();
        car.setStatus(Car.Status.SOLD);
        carRepository.save(car);

        CarAuctionUpdateMessageDTO msg = new CarAuctionUpdateMessageDTO(
                "WINNER_ACCEPTED",
                auctionId,
                auction.getCurrentPrice(),
                userId,
                List.of(),
                null
        );
        messagingTemplate.convertAndSend("/topic/car-auction/" + auctionId, msg);
    }

    //    ++++++++++++++++++++++++++++++
    public CarAuction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CarAuctionNotFoundException(auctionId));
    }


    // ===================================================================
    // WINNER REJECT
    // ===================================================================
    @Override
    @Transactional
    public void winnerReject(Long auctionId, Long userId) {

        CarAuction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CarAuctionNotFoundException(auctionId));

        if (auction.getStatus() != CarAuction.Status.ENDED) {
            throw new CarInvalidAuctionStateException("Auction is not ENDED");
        }

        List<CarBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);

        // Current winner
        CarBid winner = all.stream()
                .filter(b -> b.getStatus() == CarBid.Status.WINNING_OFFER)
                .findFirst()
                .orElseThrow(() -> new CarInvalidAuctionStateException("No winning offer"));

        if (!winner.getBidderUserId().equals(userId)) {
            throw new InvalidBidException("Not allowed");
        }

        // Reject current winner
        winner.setStatus(CarBid.Status.REJECTED);
        winner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1));
        bidRepository.save(winner);

        // -----------------------------------------------------
        // 🔥 Select next highest bidder immediately
        // -----------------------------------------------------

        List<CarBid> nextCandidates = all.stream()
                .filter(b -> b.getStatus() == CarBid.Status.PLACED)
                .toList();

        if (nextCandidates.isEmpty()) {
            // No bidders left → mark UNSOLD
            auction.setStatus(CarAuction.Status.COMPLETED);
            auction.setHighestBidderUserId(null);
            auctionRepository.save(auction);

            messagingTemplate.convertAndSend(
                    "/topic/car-auction/" + auctionId,
                    new CarAuctionUpdateMessageDTO("UNSOLD", auctionId, auction.getCurrentPrice(), null, List.of(), null)
            );
            return;
        }

        // Next highest bidder
        CarBid next = nextCandidates.get(0);
        next.setStatus(CarBid.Status.WINNING_OFFER);
        next.setOfferExpiresAt(LocalDateTime.now().plusHours(24));
        bidRepository.save(next);

        // -----------------------------------------------------
        // 🔥 Update Auction table immediately
        // -----------------------------------------------------
        auction.setHighestBidderUserId(next.getBidderUserId());
        auction.setCurrentPrice(next.getAmount());
        auctionRepository.save(auction);

        // Build Top 3 list
        List<CarAuctionUpdateMessageDTO.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
                .mapToObj(i -> new CarAuctionUpdateMessageDTO.TopBidDTO(
                        i + 1,
                        all.get(i).getBidderUserId(),
                        all.get(i).getAmount()
                ))
                .toList();

        // -----------------------------------------------------
        // 🔥 Send WS update to frontend
        // -----------------------------------------------------
        messagingTemplate.convertAndSend(
                "/topic/car-auction/" + auctionId,
                new CarAuctionUpdateMessageDTO(
                        "WINNER_CHANGED",
                        auctionId,
                        auction.getCurrentPrice(),
                        next.getBidderUserId(),
                        top3,
                        next.getOfferExpiresAt()
                )
        );

    }

    @Override
    @Transactional
    public void saveChatMessage(Long bookingId, CarChatMessageDTO incomingMsg) {

        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        try {
            ObjectMapper mapper = new ObjectMapper();

            Long buyerUserId = booking.getBuyer().getUser().getId();
            Long sellerUserId = booking.getCar().getSeller().getUser().getId();

            // Determine sender type
            String senderType;
            if (incomingMsg.getUserId().equals(buyerUserId)) {
                senderType = "BUYER";
            } else if (incomingMsg.getUserId().equals(sellerUserId)) {
                senderType = "SELLER";
            } else {
                senderType = "UNKNOWN";
            }

            // Create clean message
            Map<String, Object> storedMsg = new LinkedHashMap<>();
            storedMsg.put("userId", incomingMsg.getUserId());
            storedMsg.put("message", incomingMsg.getMessage());
            storedMsg.put("senderType", senderType);
            storedMsg.put("timestamp", OffsetDateTime.now().toString());

            // Load old conversation
            List<Map<String, Object>> conversation;
            if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
                conversation = new ArrayList<>();
            } else {
                conversation = mapper.readValue(
                        mapper.writeValueAsString(booking.getConversation()),
                        new TypeReference<List<Map<String, Object>>>() {
                        }
                );
            }

            // Add new message
            conversation.add(storedMsg);

            // Save back as JSON
            booking.setConversation(conversation);
            carBookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException("Error while saving chat message", e);
        }
    }

    @Override
    public void broadcastRunningAuctions() {
        // Example websocket broadcast
        messagingTemplate.convertAndSend(
                "/topic/car/auctions",
                "Car auctions updated"
        );


    }
}
