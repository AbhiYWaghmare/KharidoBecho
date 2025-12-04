package com.spring.jwt.laptop.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
public class LaptopRequestResponseDTO {
    private Long laptopBookingId;
    private Long laptopId;
    private Long buyerId;
    private String buyerName;
    private Long sellerId;
    private String sellerName;
    private String status;
    private String conversationJson;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private LocalDate bookingDate;
}
