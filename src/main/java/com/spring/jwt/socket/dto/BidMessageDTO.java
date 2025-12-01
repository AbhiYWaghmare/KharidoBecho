
//package com.spring.jwt.socket.dto;
//
//import java.math.BigDecimal;
//
//public record BidMessageDTO(Long auctionId, BigDecimal bidAmount) { }
// com.spring.jwt.socket.dto.BidMessageDTO
package com.spring.jwt.socket.dto;

import java.math.BigDecimal;

public record BidMessageDTO(
        Long userId,        //  add this
        Long auctionId,     // optional, or use path variable
        BigDecimal bidAmount
) {}
