package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.BidHistory;
import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.BikeBidding;
import com.spring.jwt.Bike.Repository.BidHistoryRepository;
import com.spring.jwt.Bike.Repository.WebsocketRepository;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.AuctionDto;
import com.spring.jwt.Bike.dto.BikeBidDto;
import com.spring.jwt.Bike.dto.BikeBidResponceDto;
import com.spring.jwt.exception.ResourceNotFoundException;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BikeAuctionServiceimpl implements BikeAuctionService{
    private final WebsocketRepository websocketRepository;
    private final bikeRepository bikeRepository;
    private final BidHistoryRepository bidHistoryRepository;


//    @Override
//    public BikeBidding addBikeToAuction(Bike bike, LocalDateTime closingAt) {
//        BikeBidding auction = new BikeBidding();
//        auction.setBike(bike);
//        auction.setHighestBid(0.0);
//        auction.setCreatedAt(LocalDateTime.now());
//        auction.setClosingAt(closingAt);
//        auction.setActive(true);   // explicitly set active
//        return websocketRepository.save(auction);
//    }
//
//    @Override
//    public BikeBidResponceDto processBid(BikeBidDto dto) {
//        BikeBidding bidding = websocketRepository.findById(dto.getBiddingId())
//                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
//
//        if (!bidding.isActive() || LocalDateTime.now().isAfter(bidding.getClosingAt())) {
//            throw new ResourceNotFoundException("Auction closed. No more bids allowed.");
//        }
//
//        BikeBidResponceDto res = new BikeBidResponceDto();
//        res.setBiddingId(dto.getBiddingId());
//
//        // incoming bid is higher than current highest?
//        if (dto.getBidAmount() != null && dto.getBidAmount() > (bidding.getHighestBid() == null ? 0.0 : bidding.getHighestBid())) {
//            bidding.setHighestBid(dto.getBidAmount());
//            bidding.setHighestBidderId(dto.getBidderId());
//            websocketRepository.save(bidding);
//
//            res.setHighestBid(dto.getBidAmount());
//            res.setHighestBidderId(dto.getBidderId());
//            res.setMessage("Highest bid updated!");
//        } else {
//            res.setHighestBid(bidding.getHighestBid());
//            res.setHighestBidderId(bidding.getHighestBidderId());
//            res.setMessage("Bid rejected. Too low.");
//        }
//
//        return res;
//    }
//
//    @Override
//    public void closeAuction(Integer biddingId) {
//        BikeBidding bidding = websocketRepository.findById(biddingId)
//                .orElseThrow(() -> new RuntimeException("Auction not found"));
//
//        bidding.setActive(false);
//        bidding.setClosingAt(LocalDateTime.now());
//        websocketRepository.save(bidding);
//    }
//
//    @Override
//    public BikeBidding getAuctionByBikeId(Long bikeId) {
//
//        List<BikeBidding> all = websocketRepository.findAll();
//        Optional<BikeBidding> opt = all.stream()
//                .filter(b -> b.getBike() != null && b.getBike().getBike_id() != null && b.getBike().getBike_id().equals(bikeId))
//                .findFirst();
//        return opt.orElse(null);
//    }
//@Override
//public BikeBidDto addBikeToAuction(Long bikeId, LocalDateTime closingAt) {
//
//    Bike bike = new Bike();
//    bike.setBike_id(bikeId);
//
//    BikeBidding auction = new BikeBidding();
//    auction.setBike(bike);
//    auction.setHighestBid(0.0);
//    auction.setCreatedAt(LocalDateTime.now());
//    auction.setClosingAt(closingAt);
//    auction.setActive(true);
//
//    BikeBidding saved = websocketRepository.save(auction);
//    return convertToDto(saved);
//}

//    @Override
//    public BikeBidDto processBid(BikeBidDto dto) {
//        BikeBidding bidding = websocketRepository.findById(dto.getBiddingId())
//                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
//
//        if (!bidding.isActive() || LocalDateTime.now().isAfter(bidding.getClosingAt())) {
//            dto.setMessage("Auction closed. No more bids allowed.");
//            dto.setHighestBid(bidding.getHighestBid());
//            dto.setHighestBidderId(bidding.getHighestBidderId());
//            dto.setBikeId(bidding.getBike().getBike_id());
//            return dto;
//        }
//
//        if (dto.getBidAmount() != null &&
//                dto.getBidAmount() > (bidding.getHighestBid() == null ? 0.0 : bidding.getHighestBid())) {
//            bidding.setHighestBid(dto.getBidAmount());
//            bidding.setHighestBidderId(dto.getBidderId());
//            websocketRepository.save(bidding);
//
//            dto.setHighestBid(dto.getBidAmount());
//            dto.setHighestBidderId(dto.getBidderId());
//            dto.setMessage("Highest bid updated!");
//        } else {
//            dto.setHighestBid(bidding.getHighestBid());
//            dto.setHighestBidderId(bidding.getHighestBidderId());
//            dto.setMessage("Bid rejected. Too low.");
//        }
//
//        dto.setBiddingId(bidding.getBiddingId());
//        dto.setBikeId(bidding.getBike().getBike_id());
//        return dto;
//    }
//
//
//    @Override
//    public void closeAuction(Integer biddingId) {
//        BikeBidding bidding = websocketRepository.findById(biddingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
//        bidding.setActive(false);
//        bidding.setClosingAt(LocalDateTime.now());
//        websocketRepository.save(bidding);
//    }
/*
    @Override
    public BikeBidDto getAuctionByBikeId(Long bikeId) {
        List<BikeBidding> all = websocketRepository.findAll();
        Optional<BikeBidding> opt = all.stream()
                .filter(b -> b.getBike() != null && b.getBike().getBike_id().equals(bikeId))
                .findFirst();
        return opt.map(this::convertToDto).orElse(null);
    }

 */
//    @Override
//    public BikeBidDto getAuctionByBikeId(Long bikeId) {
//        BikeBidding bidding = websocketRepository
//                .findByBike_Bike_id(bikeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Auction not found for bike id: " + bikeId));
//
//        return convertToDto(bidding);
//    }
//@Override
//public BikeBidDto getAuctionByBikeId(Long bikeId) {
//    BikeBidding bidding = websocketRepository
//            .findByBike_id(bikeId)
//            .orElseThrow(() -> new ResourceNotFoundException("Auction not found for bike id: " + bikeId));
//
//    return convertToDto(bidding);
//}
//@Override
//public BikeBidDto getAuctionByBikeId(Long bikeId) {
//    BikeBidding bidding = websocketRepository.findByBike_Bike_id(bikeId)
//            .orElseThrow(() -> new ResourceNotFoundException("Auction not found for bike id: " + bikeId));
//
//    return convertToDto(bidding);
//}

    private BikeBidDto convertToDto(BikeBidding bidding) {
        BikeBidDto dto = new BikeBidDto();
        dto.setBiddingId(bidding.getBiddingId());
        dto.setBikeId(bidding.getBike().getBike_id());
        dto.setHighestBid(bidding.getHighestBid());
        dto.setHighestBidderId(bidding.getHighestBidderId());
        dto.setMessage(bidding.isActive() ? "Auction Active" : "Auction Closed");
        return dto;
    }
    @Override
    public BikeBidDto createAuction(Long bikeId, int minutes) {

        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found"));

//        websocketRepository.findById()
//                .ifPresent(a -> { throw new RuntimeException("Auction already exists!"); });

        BikeBidding bidding = new BikeBidding();
        bidding.setBike(bike);
        bidding.setCreatedAt(LocalDateTime.now());
        bidding.setClosingAt(LocalDateTime.now().plusMinutes(minutes));
        bidding.setHighestBid(0.0);
        bidding.setActive(true);

        BikeBidding saved = websocketRepository.save(bidding);

        return convertToDto(saved);
    }

//    @Override
//    public BikeBidDto getAuctionByBikeId(Long bikeId) {
//        BikeBidding bidding = websocketRepository.findByBikeId(bikeId)
//                .orElseThrow(() -> new RuntimeException("Auction not found"));
//        return convertToDto(bidding);
//    }




    @Override
    public BikeBidDto placeBid(Integer biddingId, Long userId, Double bidAmount) {

        BikeBidding bidding = websocketRepository.findById(biddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (!bidding.isActive()) {
            throw new ResourceNotFoundException("Auction already closed");
        }

        if (bidAmount <= bidding.getHighestBid()) {
            throw new ResourceNotFoundException("Bid must be higher than current highest bid");
        }

        bidding.setHighestBid(bidAmount);
        bidding.setHighestBidderId(userId.intValue());

        BikeBidding saved = websocketRepository.save(bidding);

        return convertToDto(saved);
    }
    @Override
    public void closeAuction(Integer biddingId) {
        BikeBidding bidding = websocketRepository.findById(biddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        bidding.setActive(false);
        websocketRepository.save(bidding);
    }
    @Override
    @Transactional
    public AuctionDto processLiveBid(AuctionDto dto) {
        System.out.println("LIVE BID RECEIVED => " + dto);

        BikeBidding bidding = websocketRepository.findById(dto.getBiddingId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        AuctionDto res = new AuctionDto();
        res.setBiddingId(dto.getBiddingId());
        res.setBidderId(dto.getBidderId());
        res.setAmount(dto.getAmount());
        //  Save every bid in history
        BidHistory history = new BidHistory();
        history.setBidding(bidding);
        history.setBidderId(dto.getBidderId().intValue());
        history.setAmount(dto.getAmount());
        history.setBidTime(LocalDateTime.now());
        bidHistoryRepository.save(history);

        // Auction closed
        if (!bidding.isActive() || LocalDateTime.now().isAfter(bidding.getClosingAt())) {
            System.out.println(" Auction Closed — Ignoring Bid");
            res.setAccepted(false);
            res.setHighestBid(bidding.getHighestBid());
            res.setHighestBidderId(bidding.getHighestBidderId());
            return res;
        }

        Double currentHighest = bidding.getHighestBid() == null ? 0.0 : bidding.getHighestBid();

        if (dto.getAmount() > currentHighest) {

            System.out.println(" NEW HIGHEST BID — Saving to DB");

            bidding.setHighestBid(dto.getAmount());
            bidding.setHighestBidderId(dto.getBidderId().intValue());

            websocketRepository.save(bidding);

            res.setAccepted(true);
            res.setHighestBid(dto.getAmount());
            res.setHighestBidderId(dto.getBidderId().intValue());

        } else {
            System.out.println(" BID TOO LOW — Not Accepted");
            res.setAccepted(false);
            res.setHighestBid(bidding.getHighestBid());
            res.setHighestBidderId(bidding.getHighestBidderId());
        }

        return res;
    }

}
