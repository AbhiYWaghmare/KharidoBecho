package com.spring.jwt.Bike.Websocket.Socket.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class BikeChatMessageDto {
    private Long userId;
   // private Long bookingId;
    private String message;
    private String senderType;  //
    private String timestamp;
}
