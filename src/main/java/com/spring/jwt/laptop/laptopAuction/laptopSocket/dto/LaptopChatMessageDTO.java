package com.spring.jwt.laptop.laptopAuction.laptopSocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaptopChatMessageDTO{
    private Long requestId;
    private Long userId;
    private String message;
}
