package com.spring.jwt.socketio.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.spring.jwt.socketio.dto.LaptopAuctionCreateDTO;
import com.spring.jwt.socketio.dto.LaptopBidRequestDTO;
import com.spring.jwt.socketio.repository.LaptopBidRepository;
import com.spring.jwt.socketio.service.LaptopAuctionService;
import com.spring.jwt.socketio.service.LaptopBidService;
import com.spring.jwt.socketio.service.SocketIOService;
import com.spring.jwt.utils.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/laptop-auction")
@RestController
public class LaptopAuctionController {


    private final LaptopAuctionService auctionService;
    private final LaptopBidService bidService;

    @PostMapping("/create")
    public ResponseEntity<?> createAuction(
            @RequestBody LaptopAuctionCreateDTO dto) {

        return ResponseEntity.ok(
                auctionService.createAuction(dto));
    }

    @PostMapping("/bid")
    public ResponseEntity<?> placeBid(
            @RequestBody LaptopBidRequestDTO dto) {

        bidService.placeBid(dto);
        return ResponseEntity.ok("Bid placed");
    }
}
