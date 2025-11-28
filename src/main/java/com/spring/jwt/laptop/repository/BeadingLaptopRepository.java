    package com.spring.jwt.laptop.repository;

    import com.spring.jwt.laptop.entity.BeadingLaptop;
    import com.spring.jwt.laptop.model.AuctionStatus;
    import jakarta.persistence.LockModeType;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Lock;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.time.OffsetDateTime;
    import java.util.List;
    import java.util.Optional;

    @Repository
    public interface BeadingLaptopRepository extends JpaRepository<BeadingLaptop, Long> {
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT b FROM BeadingLaptop b WHERE b.beadingLaptopId = :id")
        Optional<BeadingLaptop> findByIdForUpdate(@Param("id") Long id);


        // Find ACTIVE auctions that are expired (closingTime <= now)
        List<BeadingLaptop> findByAuctionStatusAndClosingTimeLessThanEqual(AuctionStatus auctionStatus, OffsetDateTime time);

        List<BeadingLaptop> findByAuctionStatus(AuctionStatus auctionStatus);
    }
