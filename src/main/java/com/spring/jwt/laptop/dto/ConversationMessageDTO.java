package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMessageDTO {
    private Long senderId;               // user id (buyer or seller)
    private String senderType;           // "BUYER" or "SELLER"
    private String message;
    private OffsetDateTime timestamp;
}
