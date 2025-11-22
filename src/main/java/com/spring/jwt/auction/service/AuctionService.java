package com.spring.jwt.auction.service;

import com.spring.jwt.auction.dto.AuctionCreateDTO;
import com.spring.jwt.auction.dto.AuctionResponseDTO;
import com.spring.jwt.auction.dto.BidRequestDTO;
import com.spring.jwt.auction.dto.OfferDTO;
import com.spring.jwt.auction.entity.*;
import com.spring.jwt.auction.exception.AuctionException;
import com.spring.jwt.auction.exception.BidException;
import com.spring.jwt.auction.mapper.AuctionMapper;
import com.spring.jwt.auction.repo.AuctionRepository;
import com.spring.jwt.auction.repo.BidRepository;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepo;
    private final BidRepository bidRepo;
    private final AuctionMapper mapper;
    private final AuctionWebSocketService ws;
    private final SellerRepository sellerRepo;
    private final UserRepository userRepo;

    private static final int OFFER_CONFIRM_MINUTES = 15;

    public AuctionResponseDTO createAuction(AuctionCreateDTO dto) {
        // you can add validations: listing exists, seller owns listing etc.
        Auction a = Auction.builder()
                .listingId(dto.getListingId())
                .startPrice(dto.getStartPrice())
                .currentPrice(dto.getStartPrice())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(dto.getDurationMinutes()))
                .status(AuctionStatus.LIVE)
                .minIncrementInRupees(100)
                .build();
        a = auctionRepo.save(a);
        ws.broadcastAuctionStarted(a);
        return mapper.toDTO(a);
    }

    @Transactional
    public void placeBid(Long auctionId, Long bidderUserId, BigDecimal amount) {
        Auction auction = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new AuctionException("Auction not found: " + auctionId));

        if (auction.getStatus() != AuctionStatus.LIVE && auction.getStatus() != AuctionStatus.EXTENDED) {
            throw new AuctionException("Auction is not live");
        }

        BigDecimal floor = auction.getCurrentPrice() == null ? auction.getStartPrice() : auction.getCurrentPrice();
        BigDecimal minRequired = floor.add(BigDecimal.valueOf(auction.getMinIncrementInRupees()));
        if (amount.compareTo(minRequired) < 0) {
            throw new BidException("Bid must be at least " + minRequired);
        }

        Bid bid = Bid.builder()
                .auctionId(auctionId)
                .bidderUserId(bidderUserId)
                .amount(amount.setScale(2, BigDecimal.ROUND_HALF_UP))
                .createdAt(OffsetDateTime.now())
                .status(BidStatus.PLACED)
                .build();
        bid = bidRepo.save(bid);

        // update auction current price and highest bidder
        auction.setCurrentPrice(amount);
        auction.setHighestBidderUserId(bidderUserId);
        auctionRepo.save(auction);

        // broadcast new high bid to all watchers
        ws.broadcastNewHighBid(auction, Map.of(
                "type", "NEW_HIGH_BID",
                "auctionId", auction.getAuctionId(),
                "listingId", auction.getListingId(),
                "amount", amount,
                "bidderUserId", bidderUserId,
                "timestamp", OffsetDateTime.now()
        ));
    }

    @Transactional
    public void finalizeAuction(Long auctionId) {
        Auction auction = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new AuctionException("Auction not found"));

        if (auction.getStatus() == AuctionStatus.ENDED) return;
        auction.setStatus(AuctionStatus.ENDED);
        auctionRepo.save(auction);

        List<Bid> top3 = bidRepo.findTop3ByAuctionIdOrderByAmountDesc(auctionId);
        if (top3 == null || top3.isEmpty()) {
            ws.broadcastAuctionEnded(auction, Map.of("type", "AUCTION_ENDED_UNSOLD", "auctionId", auctionId));
            return;
        }

        // mark first candidate as pending confirmation
        Bid winnerCandidate = top3.get(0);
        winnerCandidate.setStatus(BidStatus.PENDING_CONFIRMATION);
        winnerCandidate.setOfferExpiresAt(OffsetDateTime.now().plusMinutes(OFFER_CONFIRM_MINUTES));
        bidRepo.save(winnerCandidate);

        // notify the candidate privately
        OfferDTO offer = OfferDTO.builder()
                .auctionId(auctionId)
                .listingId(auction.getListingId())
                .bidderUserId(winnerCandidate.getBidderUserId())
                .amount(winnerCandidate.getAmount())
                .offerExpiresAt(winnerCandidate.getOfferExpiresAt())
                .rank(1)
                .build();

        ws.sendOfferToUser(winnerCandidate.getBidderUserId(), offer);

        // notify public that auction ended and offer sent
        ws.broadcastAuctionEnded(auction, Map.of("type","AUCTION_ENDED_OFFER_SENT","auctionId",auctionId,"offerAmount",winnerCandidate.getAmount(),"bidderUserId",winnerCandidate.getBidderUserId()));
    }

    @Transactional
    public void confirmOffer(Long auctionId, Long bidderUserId) {
        // find the pending confirmation bid for this auction and user
        List<Bid> list = bidRepo.findByAuctionIdOrderByAmountDesc(auctionId);
        Bid candidate = list.stream()
                .filter(b -> b.getBidderUserId().equals(bidderUserId) && b.getStatus() == BidStatus.PENDING_CONFIRMATION)
                .findFirst().orElseThrow(() -> new AuctionException("No pending offer for this user"));

        // mark winner
        candidate.setStatus(BidStatus.WINNER);
        bidRepo.save(candidate);

        // mark other bids as rejected/ outbid
        list.stream().filter(b -> !b.getBidId().equals(candidate.getBidId())).forEach(b -> {
            b.setStatus(BidStatus.REJECTED);
            bidRepo.save(b);
        });

        // update mobile status to sold: integrate with Mobile repo
        // TODO: call your mobile repository/service to mark listing sold using candidate.getAuctionId -> auction.listingId
        // e.g: mobileService.markSold(auction.getListingId(), candidate.getBidderUserId());
        ws.broadcastAuctionEnded(auctionRepo.findById(auctionId).orElse(null), Map.of("type","AUCTION_SOLD","auctionId",auctionId,"winner",candidate.getBidderUserId(),"amount",candidate.getAmount()));
    }

    @Transactional
    public void rejectOfferAndPromoteNext(Bid expired) {
        // mark expired as rejected and find next candidate
        expired.setStatus(BidStatus.REJECTED);
        bidRepo.save(expired);

        List<Bid> top = bidRepo.findTop3ByAuctionIdOrderByAmountDesc(expired.getAuctionId());
        Bid next = top.stream().filter(b -> !b.getBidId().equals(expired.getBidId()) && b.getStatus() == BidStatus.PLACED).findFirst().orElse(null);

        Auction auction = auctionRepo.findById(expired.getAuctionId()).orElseThrow(() -> new AuctionException("Auction missing"));

        if (next == null) {
            // no one left: unsold
            ws.broadcastAuctionEnded(auction, Map.of("type","AUCTION_UNSOLD","auctionId",auction.getAuctionId()));
            return;
        }

        next.setStatus(BidStatus.PENDING_CONFIRMATION);
        next.setOfferExpiresAt(OffsetDateTime.now().plusMinutes(OFFER_CONFIRM_MINUTES));
        bidRepo.save(next);

        OfferDTO offer = OfferDTO.builder()
                .auctionId(next.getAuctionId())
                .listingId(auction.getListingId())
                .bidderUserId(next.getBidderUserId())
                .amount(next.getAmount())
                .offerExpiresAt(next.getOfferExpiresAt())
                .rank(2)
                .build();

        ws.sendOfferToUser(next.getBidderUserId(), offer);
        ws.broadcastAuctionEnded(auction, Map.of("type","NEXT_OFFER_PROMOTED","auctionId", auction.getAuctionId(), "nextBid", next.getAmount(), "nextUser", next.getBidderUserId()));
    }
}
