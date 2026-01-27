package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopChatMessageDTO {
    private Long messageId;
    private Long requestId;

    private Long senderUserId;
    private Long receiverUserId;

    private String senderRole;   // BUYER / SELLER
    private String message;

    private OffsetDateTime createdAt;
}
