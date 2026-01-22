package com.spring.jwt.exception.mobilechat;

public class ChatUnauthorizedSenderException extends RuntimeException {

    public ChatUnauthorizedSenderException() {
        super("User not authorized to chat on this request");
    }

    public ChatUnauthorizedSenderException(String message) {
        super(message);
    }
}
