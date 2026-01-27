package com.spring.jwt.laptop.dto;

import lombok.Data;

@Data
public class LaptopChatSendDTO {
    private Long requestId;
    private Long senderUserId;
    private String message;
}
