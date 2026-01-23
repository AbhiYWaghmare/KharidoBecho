package com.spring.jwt.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOServerRunner {

    private final SocketIOServer socketIOServer;

    /**
     * Start Socket.IO server AFTER Spring Boot is fully ready
     */
    @EventListener(ApplicationReadyEvent.class)
    public void start() {

        try {
            socketIOServer.start();
            log.info("✅ Socket.IO server STARTED on port {}",
                    socketIOServer.getConfiguration().getPort());
        } catch (Exception ex) {
            log.error("❌ Failed to start Socket.IO server", ex);
            throw ex; // fail fast – important for production
        }
    }

    /**
     * Gracefully stop Socket.IO server
     */
    @PreDestroy
    public void stop() {
        log.info("🛑 Stopping Socket.IO server...");
        socketIOServer.stop();
    }

}
