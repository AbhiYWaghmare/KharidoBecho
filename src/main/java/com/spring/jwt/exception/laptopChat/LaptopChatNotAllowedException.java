package com.spring.jwt.exception.laptopChat;

public class LaptopChatNotAllowedException extends RuntimeException{
    public LaptopChatNotAllowedException(String status) {
        super("Chat allowed only in negotiation. Current status: " + status);
    }
}
