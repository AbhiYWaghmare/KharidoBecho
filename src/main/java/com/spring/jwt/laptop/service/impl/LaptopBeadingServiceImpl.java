package com.spring.jwt.laptop.service.impl;

import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.laptop.AuctionException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.exception.mobile.BuyerNotFoundException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.laptop.dto.*;
import com.spring.jwt.laptop.entity.BeadingLaptop;
import com.spring.jwt.laptop.entity.BidLaptops;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.AuctionStatus;
import com.spring.jwt.laptop.repository.BeadingLaptopRepository;
import com.spring.jwt.laptop.repository.BidLaptopRepository;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopBeadingService;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LaptopBeadingServiceImpl implements LaptopBeadingService {

    private final BeadingLaptopRepository beadingLaptopRepo;
    private final LaptopRepository laptopRepo;
    private final BidLaptopRepository bidRepo;
    private final SellerRepository sellerRepo;
    private final BuyerRepository buyerRepo;
//    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public BeadingLaptop addLaptopForAuction(BeadingLaptopDTO dto) {
        Laptop laptop = laptopRepo.findByIdAndDeletedFalse(dto.getLaptopId())
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with id " +dto.getLaptopId()));

        Seller seller = sellerRepo.findById(dto.getSellerId())
                .orElseThrow(()-> new SellerNotFoundException(dto.getSellerId()));

        BeadingLaptop auction = new BeadingLaptop();

        auction.setLaptopId(dto.getLaptopId());
        auction.setBasePrice(dto.getBasePrice());
        auction.setMinIncrement(dto.getMinIncrement());
        auction.setSellerId(dto.getSellerId());
        auction.setDealerId(laptop.getSeller().getSellerId());
        auction.setAuctionStatus(dto.getAuctionStatus());

        auction.setCreatedAt(LocalDateTime.now());
        auction.setClosingTime(dto.getClosingTime() != null ? dto.getClosingTime() :
                LocalDateTime.now().plusHours(1));

        auction.setAuctionStatus(auction.getAuctionStatus());
        auction.setHighestBid(auction.getBasePrice());
        auction.setHighestBidderId(null);

        auction.setUniqueBeadingLaptopId(UUID.randomUUID().toString());

        return beadingLaptopRepo.save(auction);

    }

    @Override
    public BeadingLaptopWithInsDTO getLaptopAuctionById(Long beadingLaptopId) {
        BeadingLaptop auction = beadingLaptopRepo.findById(beadingLaptopId)
                .orElseThrow(() -> new AuctionException("Auction not found"));

        Laptop laptop = laptopRepo.findByIdAndDeletedFalse(auction.getLaptopId())
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found with id " +auction.getLaptopId()));

        return new BeadingLaptopWithInsDTO(auction,laptop);
    }

    @Override
    public List<BeadingLaptopWithInsDTO> getAllLiveLaptopAuctions() {

        // current IST time (automatically correct if timezone is set properly)
        LocalDateTime now = LocalDateTime.now();

        // Fetch only ACTIVE auctions
        List<BeadingLaptop> auctions = beadingLaptopRepo.findByAuctionStatus(AuctionStatus.LIVE)
                .stream()
                .filter(a -> a.getClosingTime().isAfter(now))   // Closing time not expired
                .toList();  

        if (auctions.isEmpty()) {
            throw new AuctionException("No active laptop auctions found");
        }

        return auctions.stream()
                .map(a -> new BeadingLaptopWithInsDTO(
                        a,
                        laptopRepo.findByIdAndDeletedFalse(a.getLaptopId())
                                .orElseThrow(() -> new LaptopNotFoundException(
                                        "Laptop not found for auction: " + a.getBeadingLaptopId()))
                ))
                .toList();
    }

    @Override
    public List<BeadingLaptopWithInsDTO> getLaptopAuctionsBySeller(Long sellerId) {
        List<BeadingLaptop> auctions = beadingLaptopRepo.findAll()
                .stream()
                .filter(a -> a.getSellerId().equals(sellerId))
                .toList();

        if (auctions.isEmpty()) {
            throw new SellerNotFoundException(sellerId);
        }

        return auctions.stream()
                .map(a -> new BeadingLaptopWithInsDTO(
                        a,
                        laptopRepo.findByIdAndDeletedFalse(a.getLaptopId())
                                .orElseThrow(() -> new LaptopNotFoundException(
                                        "Laptop not found for auction: " + a.getBeadingLaptopId()))
                ))
                .toList();
    }

    @Override
    @Transactional
    public BidResponseDTO placeBid(BidLaptopDTO dto) {

        // fetch auction
        BeadingLaptop auction = beadingLaptopRepo.findById(dto.getBeadingLaptopId())
                .orElseThrow(() -> new AuctionException("Auction not found"));

        Buyer buyer = buyerRepo.findById(dto.getBuyerId())
                .orElseThrow(()->new BuyerNotFoundException(dto.getBuyerId()));

        // Check status + expiry
        if (auction.getAuctionStatus() != AuctionStatus.LIVE ||
                auction.getClosingTime().isBefore(LocalDateTime.now())) {
            throw new AuctionException("Auction has already expired or closed");
        }

        // Minimum allowed bid
        double current = (auction.getHighestBid() == null)
                ? auction.getBasePrice()
                : auction.getHighestBid();

        double minAllowed = current + auction.getMinIncrement();

        if (dto.getBidAmount() < minAllowed) {
            throw new AuctionException("Bid too low");
        }

        // Create bid object
        BidLaptops bid = new BidLaptops();
        bid.setBeadingLaptop(auction);
        bid.setBidderId(dto.getBuyerId());
        bid.setBidAmount(dto.getBidAmount());
        bid.setCreatedAt(LocalDateTime.now());
        bid.setClosingAt(auction.getClosingTime());

        // Save bid (DB insert)
        bidRepo.save(bid);

        // Link bid to auction list (so JSON will show bids)
        auction.getBids().add(bid);

        // Update auction highest bid
        auction.setHighestBid(dto.getBidAmount());
        auction.setHighestBidderId(dto.getBuyerId());
        beadingLaptopRepo.save(auction);

        return new BidResponseDTO(
                auction.getBeadingLaptopId(),
                auction.getHighestBid(),
                auction.getHighestBidderId(),
                "NEW_HIGHEST_BID"
        );
    }


}
