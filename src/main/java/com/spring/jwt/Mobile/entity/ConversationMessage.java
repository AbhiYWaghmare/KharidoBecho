package com.spring.jwt.Mobile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessage {
    private Long senderId;               // user id (buyer or seller)
    private String senderType;           // "BUYER" or "SELLER"
    private String message;
    private OffsetDateTime timestamp;
    private String senderName;
}
