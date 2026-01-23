package com.spring.jwt.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.spring.jwt.socketio.controller.LaptopBidSocketController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SocketEventRegister {
    private final SocketIOServer server;
    private final LaptopBidSocketController bidController;

    @PostConstruct
    public void register() {
        server.addListeners(bidController);
    }
}
