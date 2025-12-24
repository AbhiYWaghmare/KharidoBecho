
package com.spring.jwt.car.auction.carsocket.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AuctionEventDTO(String type, Long auctionId, Long listingId,
                              BigDecimal amount, Long bidderUserId,
                              OffsetDateTime timestamp, Object extra) { }
