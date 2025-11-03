package com.spring.jwt.Mobile.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class MobileRequestResponseDTO {
    private Long requestId;
    private Long mobileId;
    private Long buyerId;
    private Long sellerId;
    private String status;
    private String conversationJson;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
