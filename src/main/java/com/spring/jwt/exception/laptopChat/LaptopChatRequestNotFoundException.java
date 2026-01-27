package com.spring.jwt.exception.laptopChat;

public class LaptopChatRequestNotFoundException extends RuntimeException {
    public LaptopChatRequestNotFoundException(Long id) {
        super("Laptop request not found with id: " + id);
    }
}
