package com.spring.jwt.socket.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SocketServerRunner {

    private final SocketIOServer socketIOServer;

    public SocketServerRunner(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @PostConstruct
    public void start() {
        socketIOServer.start();
        System.out.println(" Socket.IO server started on port 9092");
    }

    @PreDestroy
    public void stop() {
        socketIOServer.stop();
        System.out.println("Socket.IO server stopped");
    }
}
