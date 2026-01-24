package com.spring.jwt.Mobile.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private Long messageId;

    private Long requestId;

    private Long senderUserId;
    private Long receiverUserId;

    private String senderRole;   // BUYER / SELLER
    private String message;

    private OffsetDateTime createdAt;
}
