package com.spring.jwt.exception.mobilechat;

public class ChatSenderNotFoundException extends RuntimeException {
    public ChatSenderNotFoundException(Long userId) {
        super("Sender user not found with id: " + userId);
    }
}
