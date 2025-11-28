package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BidResponseDTO {
    private Long beadingLaptopId;
    private Double highestBid;
    private Long highestBidderId;
    private String message;
}
