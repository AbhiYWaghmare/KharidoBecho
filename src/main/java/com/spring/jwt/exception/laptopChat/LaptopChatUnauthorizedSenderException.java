package com.spring.jwt.exception.laptopChat;

public class LaptopChatUnauthorizedSenderException extends RuntimeException {
    public LaptopChatUnauthorizedSenderException() {
        super("User not authorized to chat on this request");
    }

    public LaptopChatUnauthorizedSenderException(String message) {
        super(message);
    }
}
