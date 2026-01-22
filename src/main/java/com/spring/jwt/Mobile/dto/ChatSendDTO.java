package com.spring.jwt.Mobile.dto;

import lombok.Data;

@Data
public class ChatSendDTO {
    private Long requestId;
    private Long senderUserId;
    private String message;
}

