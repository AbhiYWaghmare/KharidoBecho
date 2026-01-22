package com.spring.jwt.exception.mobilechat;

public class ChatRequestNotFoundException extends RuntimeException {
    public ChatRequestNotFoundException(Long id) {
        super("Mobile request not found with id: " + id);
    }
}
