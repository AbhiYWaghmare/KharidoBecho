package com.spring.jwt.exception.laptopChat;

public class LaptopChatSenderNotFoundException extends RuntimeException {
    public LaptopChatSenderNotFoundException(Long userId) {
        super("Sender user not found with id: " + userId);
    }
}
