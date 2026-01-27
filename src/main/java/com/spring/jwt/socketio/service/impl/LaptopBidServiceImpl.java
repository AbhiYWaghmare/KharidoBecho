package com.spring.jwt.socketio.service.impl;

import com.spring.jwt.auction.exception.AuctionNotFoundException;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.car.AuctionException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.socketio.dto.LaptopBidRequestDTO;
import com.spring.jwt.socketio.dto.LaptopPlacedBidDTO;
import com.spring.jwt.socketio.entity.LaptopAuction;
import com.spring.jwt.socketio.entity.LaptopBid;
import com.spring.jwt.socketio.repository.LaptopAuctionRepository;
import com.spring.jwt.socketio.repository.LaptopBidRepository;
import com.spring.jwt.socketio.service.LaptopBidService;
import com.spring.jwt.socketio.service.SocketIOService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LaptopBidServiceImpl
        implements LaptopBidService {
    private final LaptopAuctionRepository auctionRepo;
    private final LaptopBidRepository bidRepo;
    private final SocketIOService socketIOService;

    @Override
    public void placeBid(LaptopBidRequestDTO dto) {

        System.out.println(">>> BID RECEIVED: " + dto.getAuctionId()
                + " amount=" + dto.getAmount());

        LaptopAuction auction =
                auctionRepo.findById(dto.getAuctionId())
                        .orElseThrow(() -> new AuctionNotFoundException(dto.getAuctionId()));

        System.out.println(">>> AUCTION STATUS: " + auction.getStatus());


        if (auction.getStatus() != LaptopAuction.AuctionStatus.RUNNING) {
            throw new AuctionNotFoundException("Auction not running");
        }

        if (dto.getAmount()
                .compareTo(
                        auction.getCurrentPrice()
                                .add(auction.getMinIncrement())) < 0) {
            throw new AuctionException("Bid too low");
        }

        auction.setCurrentPrice(dto.getAmount());
        auction.setHighestBidderUserId(dto.getBidderUserId());
        auctionRepo.save(auction);

        LaptopBid bid = new LaptopBid();
        bid.setAuction(auction);
        bid.setBidderUserId(dto.getBidderUserId());
        bid.setAmount(dto.getAmount());
        bid.setCreatedAt(LocalDateTime.now());

        bidRepo.save(bid);

        socketIOService.sendToRoom(
                "auction_" + auction.getAuctionId(),
                "newBidPlaced",
                dto
        );
    }

    @Override
    public List<LaptopPlacedBidDTO> getTopThreeBids(Long auctionId) {

        return bidRepo
                .findTop3ByAuction_AuctionIdOrderByAmountDescCreatedAtAsc(auctionId)
                .stream()
                .map(this::mapToPlacedBidDTO)
                .toList();
    }

    @Override
    public LaptopPlacedBidDTO getTopBid(Long auctionId) {

        return bidRepo
                .findTopByAuction_AuctionIdOrderByAmountDescCreatedAtAsc(auctionId)
                .map(this::mapToPlacedBidDTO)
                .orElse(null);
    }

    private LaptopPlacedBidDTO mapToPlacedBidDTO(LaptopBid bid) {

        LaptopPlacedBidDTO dto = new LaptopPlacedBidDTO();
        dto.setAuctionId(bid.getAuction().getAuctionId());
        dto.setBidderId(bid.getBidderUserId());
        dto.setBidAmount(bid.getAmount().doubleValue());
        dto.setBidTime(bid.getCreatedAt().toString());
        return dto;
    }

}
