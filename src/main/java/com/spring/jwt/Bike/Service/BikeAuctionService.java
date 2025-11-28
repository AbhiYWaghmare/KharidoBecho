package com.spring.jwt.Bike.Service;


import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.BikeBidding;
import com.spring.jwt.Bike.dto.AuctionDto;
import com.spring.jwt.Bike.dto.BikeBidDto;

import java.util.Optional;


public interface BikeAuctionService {

   // BikeBidDto addBikeToAuction(Long bikeId, LocalDateTime closingAt);

    //BikeBidDto processBid(BikeBidDto bidDto);

    //void closeAuction(Integer biddingId);
    //BikeBidDto getAuctionByBikeId(Long bikeId);
   // BikeBidDto getAuctionByBikeId(Long bikeId);
   // Optional<BikeBidding> findByBike_BikeId(Long bikeId);
    BikeBidDto createAuction(Long bikeId, int minutes);

    //BikeBidDto getAuctionByBikeId(Long bikeId);

    BikeBidDto placeBid(Integer biddingId, Long userId, Double bidAmount);

    void closeAuction(Integer biddingId);
    AuctionDto processLiveBid(AuctionDto dto);
    //BikeBidDto GetAuctionByBikeId(Long bikeId);
    //BikeBidDto getAuctionByBikeId(Long bikeId);
}
