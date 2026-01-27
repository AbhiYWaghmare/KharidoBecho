package com.spring.jwt.socketio.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketIOService {

    private final SocketIOServer socketIOServer;

    /**
     * Send event to ALL connected clients
     */
    public void sendToAll(String event, Object data) {
        socketIOServer.getBroadcastOperations()
                .sendEvent(event, data);

        log.debug("Sent event [{}] to ALL clients", event);
    }

    /**
     * Send event to a specific room
     */
    public void sendToRoom(String room, String event, Object data) {
        socketIOServer.getRoomOperations(room)
                .sendEvent(event, data);

        log.debug("Sent event [{}] to room [{}]", event, room);
    }

    /**
     * Send event to a specific client
     */
    public void sendToClient(SocketIOClient client,
                             String event,
                             Object data) {
        client.sendEvent(event, data);

        log.debug("Sent event [{}] to client [{}]",
                event, client.getSessionId());
    }

    /**
     * Join a socket room
     */
    public void joinRoom(SocketIOClient client, String room) {
        client.joinRoom(room);
        log.info("Client [{}] joined room [{}]",
                client.getSessionId(), room);
    }

    /**
     * Leave a socket room
     */
    public void leaveRoom(SocketIOClient client, String room) {
        client.leaveRoom(room);
        log.info("Client [{}] left room [{}]",
                client.getSessionId(), room);
    }
}
