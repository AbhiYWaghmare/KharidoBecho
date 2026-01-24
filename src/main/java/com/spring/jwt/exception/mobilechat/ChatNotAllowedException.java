package com.spring.jwt.exception.mobilechat;

public class ChatNotAllowedException extends RuntimeException {
    public ChatNotAllowedException(String status) {
        super("Chat allowed only in negotiation. Current status: " + status);
    }
}