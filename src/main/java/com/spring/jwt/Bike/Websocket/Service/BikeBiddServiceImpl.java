//package com.spring.jwt.Bike.Websocket.Service;
//
//import com.spring.jwt.Bike.Entity.Bike;
//import com.spring.jwt.Bike.Repository.bikeRepository;
//import com.spring.jwt.Bike.Websocket.Dto.AuctionBidDTO;
//import com.spring.jwt.Bike.Websocket.Dto.BikeBiddDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeBidd;
//import com.spring.jwt.Bike.Websocket.Repository.BikeBiddRepository;
//import com.spring.jwt.exception.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//@Service
//@RequiredArgsConstructor
//public class BikeBiddServiceImpl implements BikeBiddService {
//
//    private final bikeRepository bikeRepository;
//    private final BikeBiddRepository bikeBiddRepository;
//
//    private BikeBiddDto convertToDto(BikeBidd bidding) {
//        BikeBiddDto dto = new BikeBiddDto();
//        dto.setBiddingId(bidding.getBiddingId());
//        dto.setBikeId(bidding.getBike().getBike_id());
//        dto.setHighestBid(bidding.getHighestBid());
//        dto.setHighestBidderId(bidding.getHighestBidderId());
//        dto.setMessage(bidding.isActive() ? "Auction Active" : "Auction Closed");
//        return dto;
//    }
//
//    @Override
//    public BikeBiddDto addBikeToAuction(Long bikeId, int minutes) {
//        Bike bike = bikeRepository.findById(bikeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Bike not found"));
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime closingTime = now.plusMinutes(minutes);
//        BikeBidd bidding = new BikeBidd();
//        bidding.setBike(bike);
//        bidding.setCreatedAt(LocalDateTime.now());
//        bidding.setClosingAt(closingTime);
//        bidding.setHighestBid(0.0);
//        bidding.setActive(true);
//
//        BikeBidd saved = bikeBiddRepository.save(bidding);
//
//        return convertToDto(saved);
//    }
//
////    public BikeBiddDto findByBike_Bike_id(Long bikeId) {
////        BikeBidd auction = bikeBiddRepository.findByBike_Bike_id(bikeId)
////                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
////
////        return convertToDto(auction);
////    }
//
//
//    public BikeBiddDto closeAuction(Integer biddingId) {
//        BikeBidd auction = bikeBiddRepository.findById(biddingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
//
//        auction.setActive(false);
//        auction.setClosingAt(LocalDateTime.now());
//
//        BikeBidd saved = bikeBiddRepository.save(auction);
//
//        return convertToDto(saved);
//    }
//    public AuctionBidDTO processBid(Integer biddingId, AuctionBidDTO bid) {
//
//        BikeBidd auction = bikeBiddRepository.findById(biddingId)
//                .orElseThrow(() -> new RuntimeException("Auction not found"));
//
//        if (!auction.isActive()) {
//            bid.setAccepted(false);
//            bid.setMessage("Auction closed");
//            return bid;
//        }
//
//        if (bid.getAmount() <= auction.getHighestBid()) {
//            bid.setAccepted(false);
//            bid.setMessage("Bid is too low");
//            return bid;
//        }
//
//        // update highest bid
//        auction.setHighestBid(bid.getAmount());
//        auction.setHighestBidderId(bid.getBidderId().intValue());
//        bikeBiddRepository.save(auction);
//
//        bid.setAccepted(true);
//        bid.setHighestBid(auction.getHighestBid());
//        bid.setHighestBidderId(auction.getHighestBidderId());
//
//        return bid;
//    }
//
//
//}
