//
//package com.spring.jwt.Bike.Websocket.Service;
//
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.spring.jwt.Bike.Entity.Bike;
//import com.spring.jwt.Bike.Entity.Bike_booking;
//import com.spring.jwt.Bike.Entity.bikeStatus;
//import com.spring.jwt.Bike.Repository.Bike_booking_repository;
//import com.spring.jwt.Bike.Websocket.Dto.BikeAuctionUpdateMessageDto;
//import com.spring.jwt.Bike.Websocket.Dto.CreateBikeAuctionRequestDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeAuction;
//import com.spring.jwt.Bike.Websocket.Entity.BikeBid;
//import com.spring.jwt.Bike.Websocket.Repository.BikeAuctionRepository;
//import com.spring.jwt.Bike.Websocket.Repository.BikeBidRepository;
//import com.spring.jwt.Bike.Repository.bikeRepository;
//import com.spring.jwt.Bike.Websocket.Service.BikeAuctionService;
//import com.spring.jwt.Bike.Websocket.Dto.BikeBidDto;
//import com.spring.jwt.Bike.Websocket.Socket.Dto.BikeChatMessageDto;
//import com.spring.jwt.Bike.dto.BikeBidResponceDto;
//
//import com.spring.jwt.exception.Bike.AuctionNotFoundException;
//import com.spring.jwt.exception.Bike.BookingNotFoundException;
//import com.spring.jwt.exception.Bike.bikeNotFoundException;
//import com.spring.jwt.exception.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.cloudinary.json.JSONArray;
//import org.cloudinary.json.JSONObject;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import jakarta.transaction.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.IntStream;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class BikeAuctionServiceImpl implements BikeAuctionService {
//
//    private final BikeAuctionRepository auctionRepository;
//    private final BikeBidRepository bidRepository;
//    private final bikeRepository bikeRepository;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final Bike_booking_repository bikeBookingRepository;
//
//    // =================================================================
//    //                      PLACE BID
//    // =================================================================
//    @Override
//    @Transactional
//    public BikeBidDto placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {
//
//        BikeAuction auction = auctionRepository.findById(auctionId)
//                .orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + auctionId));
//
//        if (auction.getStatus() != BikeAuction.AuctionStatus.ACTIVE) {
//            throw new IllegalStateException("Auction is not ACTIVE");
//        }
//
//        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
//            throw new ResourceNotFoundException("Auction already ended");
//        }
//
//        BigDecimal minAllowed = auction.getCurrentPrice() != null ?
//                auction.getCurrentPrice().add(auction.getMinIncrement()) :
//                auction.getStartPrice();
//
//        if (bidAmount.compareTo(minAllowed) < 0) {
//            throw new IllegalArgumentException("Bid must be at least " + minAllowed);
//        }
//
//        BikeBid bid = BikeBid.builder()
//                .auction(auction)
//                .bidderUserId(userId)
//                .amount(bidAmount)
//                .status(BikeBid.BidStatus.PLACED)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        bidRepository.save(bid);
//
//        auction.setCurrentPrice(bidAmount);
//        auction.setHighestBidderUserId(userId);
//        auctionRepository.save(auction);
//
//        // Send top 3 bids to websocket
//        List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
//        List<BikeBidDto> top3 = all.stream()
//                .limit(3)
//                .map(b -> new BikeBidDto(
//                        b.getBidId(),
//                        b.getAuction().getAuctionId(),
//                        b.getBidderUserId(),
//                        b.getAmount(),
//                        b.getStatus().name(),
//                        b.getCreatedAt()
//                ))
//                .toList();
//
//        messagingTemplate.convertAndSend("/topic/bike-auction/" + auctionId, top3);
//
//        // RETURN DTO (because interface requires it)
//        return new BikeBidDto(
//                bid.getBidId(),
//                auctionId,
//                userId,
//                bidAmount,
//                bid.getStatus().name(),
//                bid.getCreatedAt()
//        );
//    }
////    @Override
////    @Transactional
////    public BikeAuction createAuction(BikeAuction dto) {
////        // Fetch the bike entity from database
////        Bike bike = bikeRepository.findById(dto.getBike().getBike_id())
////                .orElseThrow(() -> new RuntimeException("Bike not found with ID: " + dto.getBike().getBike_id()));
////
////        BikeAuction a = new BikeAuction();
////        a.setBike(bike);
////        // a.setBikeId(dto.getBikeId());
////       // a.setBike_id(dto.getBike_id());
////        a.setStartPrice(dto.getStartPrice());
////        a.setCurrentPrice(dto.getStartPrice());
////        a.setMinIncrement(dto.getMinIncrement());
////
////        // if startTime missing  use NOW
////        a.setStartTime(dto.getStartTime() != null ? dto.getStartTime() : LocalDateTime.now());
////
////        a.setEndTime(dto.getEndTime());
////        a.setStatus(BikeAuction.AuctionStatus.SCHEDULED);
////        a.setHighestBidderUserId(null);
////
////        return auctionRepository.save(a);
////    }
//@Override
//@Transactional
//public BikeAuction createAuction(CreateBikeAuctionRequestDto dto) {
//
//    Bike bike = bikeRepository.findById(dto.bikeId())
//            .orElseThrow(() -> new bikeNotFoundException("Bike not found: " + dto.bikeId()));
//
//    BikeAuction a = new BikeAuction();
//    a.setBike(bike);
//    a.setStartPrice(dto.startPrice());
//    a.setCurrentPrice(dto.startPrice());
//    a.setMinIncrement(dto.minIncrement());
//    a.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
//    a.setEndTime(dto.endTime());
//    a.setStatus(BikeAuction.AuctionStatus.SCHEDULED);
//    a.setHighestBidderUserId(null);
//
//    return auctionRepository.save(a);
//}
//
//
//
//
////    @Override
////    @Transactional
////    public BikeBid placeBid(Long auctionId, Long userId, BigDecimal bidAmount) {
////        BikeAuction auction = auctionRepository.findById(auctionId)
////                .orElseThrow(() -> new ResourceNotFoundException("Auction not found: " + auctionId));
////
////        if (auction.getStatus() != BikeAuction.AuctionStatus.ACTIVE) {
////            throw new ResourceNotFoundException("Auction is not ACTIVE");
////        }
////
////        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
////            throw new ResourceNotFoundException("Auction already ended");
////        }
////
////        BigDecimal minAllowed = auction.getCurrentPrice() != null ?
////                auction.getCurrentPrice().add(auction.getMinIncrement()) :
////                auction.getStartPrice();
////
////        if (bidAmount.compareTo(minAllowed) < 0) {
////            throw new IllegalArgumentException("Bid must be at least " + minAllowed);
////        }
////
////        BikeBid bid = BikeBid.builder()
////                .auction(auction)
////                .bidderUserId(userId)
////                .amount(bidAmount)
////                .status(BikeBid.BidStatus.PLACED)
////                .createdAt(LocalDateTime.now())
////                .build();
////        bidRepository.save(bid);
////
////        auction.setCurrentPrice(bidAmount);
////        auction.setHighestBidderUserId(userId);
////        auctionRepository.save(auction);
////
////        // Top 3 bids
////        List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
////        List<BikeBidDto> top3 = IntStream.range(0, Math.min(3, all.size()))
////                .mapToObj(i -> BikeBidDto.builder()
////                        .bidId(all.get(i).getBidId())
////                        .bidderUserId(all.get(i).getBidderUserId())
////
////                        .amount(all.get(i).getAmount())
////                        .build())
////                .toList();
////
////        // WebSocket push
////        messagingTemplate.convertAndSend("/topic/bike-auction/" + auctionId, top3);
////
////        return BikeBidDto.builder()
////                .auctionId(auctionId)
////                .amount(bidAmount)
////                .bidderUserId(userId)
////                .build();
////    }
//
//    // =================================================================
//    //                      START DUE AUCTIONS
//    // =================================================================
//    @Override
//    @Transactional
//    public void startDueAuctions() {
//        LocalDateTime now = LocalDateTime.now();
//        List<BikeAuction> toStart = auctionRepository.findByStatusAndStartTimeLessThanEqual(
//                BikeAuction.AuctionStatus.SCHEDULED, now);
//
//        for (BikeAuction a : toStart) {
//            a.setStatus(BikeAuction.AuctionStatus.ACTIVE);
//            if (a.getCurrentPrice() == null) {
//                a.setCurrentPrice(a.getStartPrice());
//            }
//            auctionRepository.save(a);
//            messagingTemplate.convertAndSend("/topic/bike-auction/" + a.getAuctionId(), "AUCTION_STARTED");
//        }
//    }
//
//    // =================================================================
//    //                      END DUE AUCTIONS
//    // =================================================================
////    @Override
////    @Transactional
////    public void endDueAuctions() {
////        LocalDateTime now = LocalDateTime.now();
////        List<BikeAuction> toEnd = auctionRepository.findExpiredAuctions(BikeAuction.AuctionStatus.ACTIVE, now);
////
////        for (BikeAuction a : toEnd) {
////            a.setStatus(BikeAuction.AuctionStatus.ENDED);
////            auctionRepository.save(a);
////
////            // Mark winner
////            List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());
////
////        }
////
////    }
//    @Override
//    @Transactional
//    public void endDueAuctions() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // Find all expired ACTIVE auctions
//        List<BikeAuction> toEnd = auctionRepository.findExpiredAuctions(
//                BikeAuction.AuctionStatus.ACTIVE, now
//        );
//
//        for (BikeAuction a : toEnd) {
//
//            // 1. Mark auction as ENDED
//            a.setStatus(BikeAuction.AuctionStatus.ENDED);
//            auctionRepository.save(a);
//
//            // 2. Fetch all bids in descending order (highest first)
//            List<BikeBid> allBids = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());
//
//            if (allBids.isEmpty()) {
//                // No bids submitted no winner
//                continue;
//            }
//
//            // 3. Top bid becomes WINNING_OFFER
//            BikeBid winner = allBids.get(0);
//            winner.setStatus(BikeBid.BidStatus.WINNING_OFFER);
//            winner.setOfferExpiresAt(LocalDateTime.now().plusHours(24));
//            bidRepository.save(winner);
//
//            // 4. Others become LOST
//            for (int i = 1; i < allBids.size(); i++) {
//                BikeBid b = allBids.get(i);
//                b.setStatus(BikeBid.BidStatus.LOST);
//                bidRepository.save(b);
//            }
//        }
//    }
//
//    // =================================================================
////                   PROCESS EXPIRED OFFERS (BIKE)
//// =================================================================
//    @Override
//    @Transactional
//    public void processExpiredOffers() {
//
//        LocalDateTime now = LocalDateTime.now();
//
//        // Get all BIKE auctions that ended
//        List<BikeAuction> auctions = auctionRepository.findByStatusAndEndTimeLessThanEqual(
//                BikeAuction.AuctionStatus.ENDED, now
//        );
//
//        for (BikeAuction a : auctions) {
//
//            // Get expired winning offers
//            List<BikeBid> expired = bidRepository.findExpiredOffers(
//                    a, BikeBid.BidStatus.WINNING_OFFER, now
//            );
//
//            if (expired.isEmpty()) continue;
//
//            // Mark all expired offers as EXPIRED
//            for (BikeBid b : expired) {
//                b.setStatus(BikeBid.BidStatus.EXPIRED);
//                bidRepository.save(b);
//            }
//
//            // Fetch all bids, sorted
//            List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(a.getAuctionId());
//
//            // Candidates = all PLACED bids except expired ones
//            List<BikeBid> candidates = all.stream()
//                    .filter(b -> b.getStatus() == BikeBid.BidStatus.PLACED)
//                    .toList();
//
//
//            // CASE 1: NO MORE BIDDERS → BIKE UNSOLD
//
//            if (candidates.isEmpty()) {
//
//                a.setStatus(BikeAuction.AuctionStatus.COMPLETED);
//                auctionRepository.save(a);
//
//                // Send UNSOLD update
//                BikeAuctionUpdateMessageDto msg = new BikeAuctionUpdateMessageDto(
//                        "UNSOLD",
//                        a.getAuctionId(),
//                        a.getCurrentPrice(),
//                        a.getHighestBidderUserId(),
//                        List.of(),
//                        null
//                );
//
//                messagingTemplate.convertAndSend("/topic/bike-auction/" + a.getAuctionId(), msg);
//
//                continue;
//            }
//
//
//            // CASE 2: NEXT HIGHEST BIDDER BECOMES NEW WINNING OFFER
//
//            BikeBid newWinner = candidates.get(0);
//            newWinner.setStatus(BikeBid.BidStatus.WINNING_OFFER);
//
//            LocalDateTime newExpire = LocalDateTime.now().plusHours(24);
//            newWinner.setOfferExpiresAt(newExpire);
//            bidRepository.save(newWinner);
//
//            // Build TOP 3 bid list
//            List<BikeAuctionUpdateMessageDto.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
//                    .mapToObj(i -> new BikeAuctionUpdateMessageDto.TopBidDTO(
//                            i + 1,
//                            all.get(i).getBidderUserId(),
//                            all.get(i).getAmount()
//                    ))
//                    .toList();
//
//            // WebSocket message
//            BikeAuctionUpdateMessageDto msg = new BikeAuctionUpdateMessageDto(
//                    "WINNER_CHANGED",
//                    a.getAuctionId(),
//                    a.getCurrentPrice(),
//                    newWinner.getBidderUserId(),
//                    top3,
//                    newExpire
//            );
//
//            messagingTemplate.convertAndSend("/topic/bike-auction/" + a.getAuctionId(), msg);
//        }
//    }
//
//
//    // =================================================================
////                   WINNER ACCEPT (BIKE)
//// =================================================================
//    @Override
//    @Transactional
//    public void winnerAccept(Long auctionId, Long userId) {
//
//        BikeAuction auction = auctionRepository.findById(auctionId)
//                .orElseThrow(() -> new AuctionNotFoundException("Bike Auction not found: " + auctionId));
//
//        if (auction.getStatus() != BikeAuction.AuctionStatus.ENDED) {
//            throw new ResourceNotFoundException("Bike Auction is not ENDED");
//        }
//
//        List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
//
//        BikeBid winner = all.stream()
//                .filter(b -> b.getStatus() == BikeBid.BidStatus.WINNING_OFFER)
//                .findFirst()
//                .orElseThrow(() -> new ResourceNotFoundException("No WINNING_OFFER found"));
//
//        if (!winner.getBidderUserId().equals(userId)) {
//            throw new ResourceNotFoundException("User is NOT the bike auction winner");
//        }
//
//        winner.setStatus(BikeBid.BidStatus.ACCEPTED);
//        bidRepository.save(winner);
//
//        auction.setStatus(BikeAuction.AuctionStatus.COMPLETED);
//        auctionRepository.save(auction);
//
////        Bike bike = bikeRepository.findById(auction.getBikeId())
////                .orElseThrow(() -> new bikeNotFoundException("Bike not found: " + auction.getBikeId()));
//////
//        Bike bike = bikeRepository.findById(auction.getBike().getBike_id())
//                .orElseThrow(() -> new bikeNotFoundException("Bike not found: " + auction.getBike().getBike_id()));
//
//
//        bike.setStatus(bikeStatus.DELETED);
//        bikeRepository.save(bike);
//
//        BikeAuctionUpdateMessageDto msg = new BikeAuctionUpdateMessageDto(
//                "WINNER_ACCEPTED",
//                auctionId,
//                auction.getCurrentPrice(),
//                userId,
//                List.of(),
//                null
//        );
//
//        messagingTemplate.convertAndSend("/topic/bike-auction/" + auctionId, msg);
//    }
//
//
//
//
//
//    // =================================================================
////                   WINNER REJECT (BIKE)
//// =================================================================
////    @Override
////    @Transactional
////    public void winnerReject(Long auctionId, Long userId) {
////
////        BikeAuction auction = auctionRepository.findById(auctionId)
////                .orElseThrow(() -> new IllegalArgumentException("Bike Auction not found: " + auctionId));
////
////        if (auction.getStatus() != BikeAuction.AuctionStatus.ENDED) {
////            throw new IllegalStateException("Bike Auction is not ENDED");
////        }
////
////        List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
////
////        BikeBid winner = all.stream()
////                .filter(b -> b.getStatus() == BikeBid.BidStatus.WINNING_OFFER)
////                .findFirst()
////                .orElseThrow(() -> new IllegalStateException("No WINNING_OFFER found"));
////
////        if (!winner.getBidderUserId().equals(userId)) {
////            throw new IllegalArgumentException("User is NOT the winning bidder");
////        }
////
////        winner.setStatus(BikeBid.BidStatus.REJECTED);
////        winner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1)); // expire now
////        bidRepository.save(winner);
////
////        processExpiredOffers(); // select next highest bidder
////    }
//
//    @Override
//    @Transactional
//    public void winnerReject(Long auctionId, Long userId) {
//
//        BikeAuction auction = auctionRepository.findById(auctionId)
//                .orElseThrow(() -> new AuctionNotFoundException("Bike Auction not found: " + auctionId));
//
//        if (auction.getStatus() != BikeAuction.AuctionStatus.ENDED) {
//            throw new IllegalStateException("Bike Auction is not ENDED");
//        }
//
//        List<BikeBid> all = bidRepository.findAllByAuctionOrderByAmountDesc(auctionId);
//
//        BikeBid currentWinner = all.stream()
//                .filter(b -> b.getStatus() == BikeBid.BidStatus.WINNING_OFFER)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("No WINNING_OFFER found"));
//
//        if (!currentWinner.getBidderUserId().equals(userId)) {
//            throw new IllegalArgumentException("User is NOT the winning bidder");
//        }
//
//        // Mark current winner rejected
//        currentWinner.setStatus(BikeBid.BidStatus.REJECTED);
//        currentWinner.setOfferExpiresAt(LocalDateTime.now().minusMinutes(1)); //
//        bidRepository.save(currentWinner);
//
//        // Promote next highest PLACED bid
//        BikeBid next = all.stream()
//                .filter(b -> b.getStatus() == BikeBid.BidStatus.LOST)
//                .findFirst()
//                .orElse(null);
//
//        if (next == null) {
//            // No more bidders  UNSOLD / completed
//            auction.setStatus(BikeAuction.AuctionStatus.COMPLETED);
//            auctionRepository.save(auction);
//
//            BikeAuctionUpdateMessageDto msg = new BikeAuctionUpdateMessageDto(
//                    "UNSOLD",
//                    auction.getAuctionId(),
//                    auction.getCurrentPrice(),
//                    auction.getHighestBidderUserId(),
//                    List.of(),
//                    null
//            );
//            messagingTemplate.convertAndSend("/topic/bike-auction/" + auction.getAuctionId(), msg);
//            return;
//        }
//
//        // Promote the next bidder
//        next.setStatus(BikeBid.BidStatus.WINNING_OFFER);
//        LocalDateTime newExpiry = LocalDateTime.now().plusHours(24);
//        next.setOfferExpiresAt(newExpiry);
//        bidRepository.save(next);
//        // update the hoghest bidder and highest bid amount
//        auction.setHighestBidderUserId(next.getBidderUserId());
//        auction.setCurrentPrice(next.getAmount());
//        auctionRepository.save(auction);
//
//        // send WebSocket update (similar to your processExpiredOffers)
//        List<BikeAuctionUpdateMessageDto.TopBidDTO> top3 = IntStream.range(0, Math.min(3, all.size()))
//                .mapToObj(i -> new BikeAuctionUpdateMessageDto.TopBidDTO(
//                        i + 1,
//                        all.get(i).getBidderUserId(),
//                        all.get(i).getAmount()
//                ))
//                .toList();
//
//        BikeAuctionUpdateMessageDto msg = new BikeAuctionUpdateMessageDto(
//                "WINNER_CHANGED",
//                auction.getAuctionId(),
//                auction.getCurrentPrice(),
//                next.getBidderUserId(),
//                top3,
//                newExpiry
//        );
//        messagingTemplate.convertAndSend("/topic/bike-auction/" + auction.getAuctionId(), msg);
//    }
//    @Override
//    public BikeAuction getAuctionById(Long id) {
//
//        return auctionRepository.findById(id)
//                .orElseThrow(() -> new AuctionNotFoundException("Bike Auction not found: " + id));
//    }
//
//    @Override
//    public List<BikeAuction> listAuctionsByStatus(String status) {
//
//        if (status == null || status.isBlank()) {
//            return auctionRepository.findAll();
//        }
//
//        BikeAuction.AuctionStatus st = BikeAuction.AuctionStatus.valueOf(status);
//
//        return auctionRepository.findAll()
//                .stream()
//                .filter(a -> a.getStatus() == st)
//                .toList();
//    }
///*
//@Override
//public void saveChatMessage(Long bookingId, BikeChatMessageDto message) {
//
//    Bike_booking booking = bikeBookingRepository.findById(bookingId)
//            .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//    try {
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Convert JSON -> List
//        List<BikeChatMessageDto> conversation;
//
//        if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
//            conversation = new ArrayList<>();
//        } else {
//            conversation = mapper.readValue(
//                    booking.getConversation(),
//                    new TypeReference<List<BikeChatMessageDto>>() {}
//            );
//        }
//
//        // Add new message
//        conversation.add(message);
//
//        // List -> JSON
//        String updatedJson = mapper.writeValueAsString(conversation);
//
//        booking.setConversation(updatedJson);
//
//        bikeBookingRepository.save(booking);
//
//    } catch (Exception e) {
//        throw new RuntimeException("Error while saving chat message", e);
//    }
//}
//
// */
//    /*
//    @Override
//    @Transactional
//    public void saveChatMessage(Long bookingId, BikeChatMessageDto message) {
//
//        Bike_booking booking = bikeBookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        try {
//
//            // ---- AUTO SET SENDER TYPE ----
//            Long buyerUserId = booking.getBuyer().getUser().getId();
//            Long sellerUserId = booking.getBike().getSeller().getUser().getId();
//
//            if (message.getUserId().equals(buyerUserId)) {
//                message.setSenderType("BUYER");
//            } else if (message.getUserId().equals(sellerUserId)) {
//                message.setSenderType("SELLER");
//            } else {
//                message.setSenderType("UNKNOWN");
//            }
//
//            // ---- SET TIMESTAMP ----
//            message.setTimestamp(java.time.OffsetDateTime.now().toString());
//
//            ObjectMapper mapper = new ObjectMapper();
//
//            List<BikeChatMessageDto> conversation;
//
//            if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
//                conversation = new ArrayList<>();
//            } else {
//                conversation = mapper.readValue(
//                        booking.getConversation(),
//                        new TypeReference<List<BikeChatMessageDto>>() {}
//                );
//            }
//
//            // Add message
//            conversation.add(message);
//
//            // List → JSON
//            booking.setConversation(mapper.writeValueAsString(conversation));
//
//            bikeBookingRepository.save(booking);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error while saving chat message", e);
//        }
//    }
//
//     */
//@Override
//@Transactional
//public void saveChatMessage(Long bookingId, BikeChatMessageDto incomingMsg) {
//
//    Bike_booking booking = bikeBookingRepository.findById(bookingId)
//            .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
//
//    try {
//        ObjectMapper mapper = new ObjectMapper();
//
//
////        Long buyerUserId = booking.getBuyer().getUser().getUserId();
////
////        Long sellerUserId = booking.getBike().getSeller().getUser().getUserId();
//        Long buyerUserId = booking.getBuyer().getUser().getId();
//
//        Long sellerUserId = booking.getBike().getSeller().getUser().getId();
//
//        //   Determine sender type
//        String senderType;
//        if (incomingMsg.getUserId().equals(buyerUserId)) {
//            senderType = "BUYER";
//        } else if (incomingMsg.getUserId().equals(sellerUserId)) {
//            senderType = "SELLER";
//        } else {
//            senderType = "UNKNOWN";
//        }
//
//        //  Create message
//        BikeChatMessageDto storedMsg = new BikeChatMessageDto();
//        storedMsg.setUserId(incomingMsg.getUserId());
//        storedMsg.setMessage(incomingMsg.getMessage());
//        storedMsg.setSenderType(senderType);
//        storedMsg.setTimestamp(java.time.OffsetDateTime.now().toString());
//
//
//
//        // Load old conversation
//        List<BikeChatMessageDto> conversation;
//
//        if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
//            conversation = new ArrayList<>();
//        } else {
//            conversation = mapper.readValue(
//                    booking.getConversation(),
//                    new TypeReference<List<BikeChatMessageDto>>() {}
//            );
//        }
//
//        //  Add new clean message
//        conversation.add(storedMsg);
//
//        //  Save JSON
//        booking.setConversation(mapper.writeValueAsString(conversation));
//        bikeBookingRepository.save(booking);
//
//    } catch (Exception e) {
//        throw new RuntimeException("Error while saving chat message", e);
//    }
//}
//
//
//
//
//
//}
