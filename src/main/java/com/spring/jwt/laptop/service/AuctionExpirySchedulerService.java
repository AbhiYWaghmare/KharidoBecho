package com.spring.jwt.laptop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuctionExpirySchedulerService {
    public void closeExpiredAuctions();
}
