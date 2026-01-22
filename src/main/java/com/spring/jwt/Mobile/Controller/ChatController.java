package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.Services.MobileRequestChatService;
import com.spring.jwt.Mobile.Services.impl.MobileRequestChatServiceImpl;
import com.spring.jwt.Mobile.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MobileRequestChatServiceImpl chatService;

    @GetMapping("/{requestId}")
    public ResponseEntity<List<ChatMessageDTO>> getChat(@PathVariable Long requestId) {
        return ResponseEntity.ok(chatService.getChat(requestId));
    }
}

