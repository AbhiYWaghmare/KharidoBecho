package com.spring.jwt.socket.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMessageSocketDTO {

    private Long requestId;
    private Long senderId;
    private String senderType; // BUYER / SELLER
    private String senderName;
    private String message;
    private OffsetDateTime timestamp;
}
