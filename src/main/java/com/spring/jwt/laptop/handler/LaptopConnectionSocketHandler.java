package com.spring.jwt.laptop.handler;

import com.corundumstudio.socketio.SocketIOServer;
import com.spring.jwt.laptop.dto.LaptopChatMessageDTO;
import com.spring.jwt.laptop.dto.LaptopChatSendDTO;
import com.spring.jwt.laptop.service.LaptopChatService;
import com.spring.jwt.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LaptopConnectionSocketHandler {
    private final SocketIOServer server;
    private final UserRepository userRepo;
    private final LaptopChatService chatService;

    public LaptopConnectionSocketHandler(
            SocketIOServer server,
            UserRepository userRepo,
            LaptopChatService chatService) {

        this.server = server;
        this.userRepo = userRepo;
        this.chatService = chatService;
    }

    @PostConstruct
    public void register() {

        /* ---------------- CONNECT ---------------- */
        server.addConnectListener(client -> {

            String userIdStr = client.getHandshakeData()
                    .getSingleUrlParam("userId");

            // If no userId -> allow connection (for auction module)
            if (userIdStr == null) {
                System.out.println("ℹ️ Socket connected without userId (non-chat client)");
                return;
            }

            Long userId;
            try {
                userId = Long.valueOf(userIdStr);
            } catch (Exception e) {
                client.sendEvent("socket_error", "Invalid userId format");
                client.set("INVALID", true);
                return;
            }

            if (userRepo.findById(userId).isEmpty()) {
                client.sendEvent("socket_error", "User not found in database");
                client.set("INVALID", true);
                return;
            }

            client.joinRoom("user_" + userId);
            client.set("userId", userId);

            System.out.println("✅ Laptop User connected: " + userId);
        });



        /* ---------------- LAPTOP CHAT ---------------- */
        server.addEventListener(
                "send_laptop_chat_message",
                LaptopChatSendDTO.class,
                (client, data, ackRequest) -> {

                    Boolean invalid = client.get("INVALID");

                    if (Boolean.TRUE.equals(invalid)) {
                        client.sendEvent("socket_error", "Invalid connection");
                        return;
                    }

                    try {
                        LaptopChatMessageDTO savedMessage =
                                chatService.sendMessage(data);

                        server.getRoomOperations(
                                        "user_" + savedMessage.getSenderUserId())
                                .sendEvent(
                                        "receive_laptop_chat_message",
                                        savedMessage);

                        server.getRoomOperations(
                                        "user_" + savedMessage.getReceiverUserId())
                                .sendEvent(
                                        "receive_laptop_chat_message",
                                        savedMessage);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        client.sendEvent("socket_error", ex.getMessage());
                    }
                }
        );
    }
}
