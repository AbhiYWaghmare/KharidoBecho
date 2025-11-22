package com.spring.jwt.socket.dto;
//
//import java.time.OffsetDateTime;
//
//public record ChatMessageDTO(Long requestId, Long senderUserId, String message, OffsetDateTime timestamp) { }
public record ChatMessageDTO(
        Long userId,
        String message
) {}
