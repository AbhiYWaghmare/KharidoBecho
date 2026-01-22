package com.spring.jwt.Mobile.Services;

import com.spring.jwt.Mobile.dto.ChatMessageDTO;
import com.spring.jwt.Mobile.dto.ChatSendDTO;
import com.spring.jwt.Mobile.entity.MobileRequest;
import com.spring.jwt.entity.User;

import java.util.List;

public interface MobileRequestChatService {

    ChatMessageDTO sendMessage(ChatSendDTO dto);

    List<ChatMessageDTO> getChat(Long requestId);

    void validateSender(MobileRequest request, User sender);
}
