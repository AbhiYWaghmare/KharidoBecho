package com.spring.jwt.laptop.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class LaptopRequestResponseDTO {
    private Long requestId;
    private Long laptopId;
    private Long buyerId;
    private Long sellerId;
    private String status;
    private String conversationJson;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
