package com.spring.jwt.laptop.laptopAuction.laptopSocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LaptopChatMessageDTO{
    private Long userId;
    private String message;
}
