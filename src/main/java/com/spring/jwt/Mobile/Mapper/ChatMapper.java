package com.spring.jwt.Mobile.Mapper;

import com.spring.jwt.Mobile.dto.ChatMessageDTO;
import com.spring.jwt.Mobile.entity.ChatMessage;
import com.spring.jwt.entity.User;

public class ChatMapper {

    public static ChatMessageDTO toDTO(
            ChatMessage msg,
            String senderRole,
            Long receiverUserId
    ) {
        return ChatMessageDTO.builder()
                .messageId(msg.getId())
                .requestId(msg.getRequest().getRequestId())
                .senderUserId(msg.getSender().getId())
                .receiverUserId(receiverUserId)
                .senderRole(senderRole)
                .message(msg.getMessage())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}


