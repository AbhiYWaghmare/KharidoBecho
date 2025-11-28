package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.BikeBidding;
import com.spring.jwt.Bike.dto.BikeBidDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WebsocketRepository extends JpaRepository<BikeBidding, Integer> {
    List<BikeBidding> findByClosingAtAfter(LocalDateTime now);
    // find currently active auctions
    //List<BikeBidding> findByActiveTrue();
    //Optional<BikeBidding> findByBike_Bike_id(Long bikeId);
    // get auction by bikeId
    //Optional<BikeBidding> findByBike_BikeId(Long bikeId);

    // get active auction
    List<BikeBidding> findByActiveTrue();

    // get expired but active (for scheduler)
    List<BikeBidding> findByActiveTrueAndClosingAtBefore(LocalDateTime now);
   // BikeBidDto GetAuctionByBikeId(Long bikeId);
//   Optional<BikeBidding> findByBike_Bike_id(Long bike_id);
    @Query("SELECT b FROM BikeBidding b WHERE b.bike.bike_id = :bikeId")
    Optional<BikeBidding> findByBikeId(@Param("bikeId") Long bikeId);

}
