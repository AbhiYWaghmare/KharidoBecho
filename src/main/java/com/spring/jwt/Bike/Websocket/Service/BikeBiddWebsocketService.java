//package com.spring.jwt.Bike.Websocket.Service;
//
//import com.spring.jwt.Bike.Websocket.Dto.AuctionBidDto;
//import com.spring.jwt.Bike.Websocket.Entity.BikeBidd;
//import com.spring.jwt.Bike.Websocket.Repository.BikeBiddRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class BikeBiddWebsocketService implements BikeBiddService {
//    private final BikeBiddRepository bikeBiddRepository;
//
//    public AuctionBidDto processBid(Integer biddingId, AuctionBidDto bid) {
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
//}
