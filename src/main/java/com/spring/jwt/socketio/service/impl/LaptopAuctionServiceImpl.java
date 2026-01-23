package com.spring.jwt.socketio.service.impl;

import com.spring.jwt.exception.car.AuctionException;
import com.spring.jwt.exception.laptop.LaptopNotFoundException;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.socketio.dto.LaptopAuctionCreateDTO;
import com.spring.jwt.socketio.dto.LaptopAuctionResponseDTO;
import com.spring.jwt.socketio.entity.LaptopAuction;
import com.spring.jwt.socketio.repository.LaptopAuctionRepository;
import com.spring.jwt.socketio.repository.LaptopBidRepository;
import com.spring.jwt.socketio.service.LaptopAuctionService;
import com.spring.jwt.socketio.service.LaptopBidService;
import com.spring.jwt.socketio.service.SocketIOService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class LaptopAuctionServiceImpl implements LaptopAuctionService {

    private final LaptopAuctionRepository auctionRepo;
    private final LaptopRepository laptopRepo;
    private final SocketIOService socketIOService;

    @Override
    public LaptopAuctionResponseDTO createAuction(
            LaptopAuctionCreateDTO dto) {

        Laptop laptop = laptopRepo.findByIdAndDeletedFalse(dto.getLaptopId())
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found"));

        if (auctionRepo.existsByLaptop_IdAndStatus(
                laptop.getId(), LaptopAuction.AuctionStatus.RUNNING)) {
            throw new AuctionException("Auction already running");
        }

        LaptopAuction auction = new LaptopAuction();
        auction.setLaptop(laptop);
        auction.setStartPrice(dto.getStartPrice());
        auction.setCurrentPrice(dto.getStartPrice());
        auction.setMinIncrement(dto.getMinIncrement());
        auction.setStartTime(dto.getStartTime());
        auction.setEndTime(dto.getEndTime());
        auction.setStatus(LaptopAuction.AuctionStatus.SCHEDULED);

        LaptopAuction saved = auctionRepo.save(auction);

        socketIOService.sendToAll(
                "auctionScheduled", saved.getAuctionId());

        LaptopAuctionResponseDTO res = new LaptopAuctionResponseDTO();
        res.setAuctionId(saved.getAuctionId());
        res.setLaptopId(laptop.getId());
        res.setCurrentPrice(saved.getCurrentPrice());
        res.setStartTime(saved.getStartTime());
        res.setEndTime(saved.getEndTime());
        res.setStatus(saved.getStatus().name());

        return res;
    }
}

