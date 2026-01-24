//*********** Without Annotation Implements ************** //



package com.spring.jwt.socket.handler;

//import com.corundumstudio.socketio.AckRequest;
//import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.annotation.OnConnect;
//import com.corundumstudio.socketio.annotation.OnDisconnect;
//import com.corundumstudio.socketio.annotation.OnEvent;
import com.spring.jwt.Mobile.Services.impl.MobileRequestChatServiceImpl;
import com.spring.jwt.Mobile.dto.ChatMessageDTO;
import com.spring.jwt.Mobile.dto.ChatSendDTO;
import com.spring.jwt.Mobile.Services.MobileRequestChatService;
import com.spring.jwt.repository.UserRepository;
import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ConnectionSocketHandler {

    private final SocketIOServer server;
    private final UserRepository userRepo;
    private final MobileRequestChatService chatService;

    public ConnectionSocketHandler(
            SocketIOServer server,
            UserRepository userRepo,
            MobileRequestChatService chatService) {
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

            if (userIdStr == null) {
                System.out.println("❌ Connection rejected: userId missing");
                client.sendEvent("socket_error", "userId is required");
                client.disconnect();
                return;
            }

            Long userId;

            try {
                userId = Long.valueOf(userIdStr);
            } catch (Exception e) {
                System.out.println("❌ Invalid userId format: " + userIdStr);
                client.sendEvent("socket_error", "Invalid userId format");
                client.disconnect();
                return;
            }

            if (userRepo.findById(userId).isEmpty()) {
                System.out.println("❌ Connection rejected: User not found -> " + userId);

                // 🔥 IMPORTANT: flush event before disconnect
                client.sendEvent("socket_error", "User not found in database");

                // small delay to ensure delivery
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}

                client.disconnect();
                return;
            }

            client.joinRoom("user_" + userId);
            client.set("userId", userId);

            System.out.println("✅ User connected: " + userId);
        });



        /* ---------------- DISCONNECT ---------------- */
        server.addDisconnectListener(client -> {

            Boolean invalid = client.get("INVALID");

            if (Boolean.TRUE.equals(invalid)) {
                System.out.println("❌ Invalid client disconnected");
            } else {
                System.out.println("❌ User disconnected: " + client.get("userId"));
            }
        });


        /* ---------------- CHAT MESSAGE ---------------- */
        server.addEventListener(
                "send_chat_message",
                ChatSendDTO.class,
                (client, data, ackRequest) -> {

                    Boolean invalid = client.get("INVALID");

                    if (Boolean.TRUE.equals(invalid)) {
                        client.sendEvent("socket_error", "Invalid connection");
                        return;
                    }

                    try {
                        ChatMessageDTO savedMessage =
                                chatService.sendMessage(data);

                        server.getRoomOperations("user_" + savedMessage.getSenderUserId())
                                .sendEvent("receive_chat_message", savedMessage);

                        server.getRoomOperations("user_" + savedMessage.getReceiverUserId())
                                .sendEvent("receive_chat_message", savedMessage);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        client.sendEvent("socket_error", ex.getMessage());
                    }
                }
        );



    }
}





//************************************ With Annotation But Not work *************************************//


//package com.spring.jwt.socket.handler;
//
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.annotation.OnConnect;
//import com.corundumstudio.socketio.annotation.OnDisconnect;
//import com.corundumstudio.socketio.annotation.OnEvent;
//import com.spring.jwt.Mobile.Services.MobileRequestChatService;
//import com.spring.jwt.Mobile.dto.ChatMessageDTO;
//import com.spring.jwt.Mobile.dto.ChatSendDTO;
//import com.spring.jwt.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ConnectionSocketHandler {
//
//    private final SocketIOServer server;
//    private final UserRepository userRepo;
//    private final MobileRequestChatService chatService;
//
//    /* ---------------- CONNECT ---------------- */
//    @OnConnect
//    public void onConnect(SocketIOClient client) {
//
//        String userIdStr = client.getHandshakeData()
//                .getSingleUrlParam("userId");
//
//        if (userIdStr == null) {
//            client.sendEvent("socket_error", "UserId missing");
//            client.disconnect();
//            return;
//        }
//
//        Long userId = Long.valueOf(userIdStr);
//
//        if (userRepo.findById(userId).isEmpty()) {
//            client.sendEvent("socket_error", "Invalid user");
//            client.disconnect();
//            return;
//        }
//
//        client.joinRoom("user_" + userId);
//
//        System.out.println("✅ User connected: " + userId);
//    }
//
//    /* ---------------- DISCONNECT ---------------- */
//    @OnDisconnect
//    public void onDisconnect(SocketIOClient client) {
//        System.out.println("❌ User disconnected: " + client.getSessionId());
//    }
//
//    /* ---------------- CHAT MESSAGE ---------------- */
//    @OnEvent("send_chat_message")
//    public void onChatMessage(SocketIOClient client, ChatSendDTO data) {
//
//        try {
//            ChatMessageDTO savedMessage = chatService.sendMessage(data);
//
//            server.getRoomOperations("user_" + savedMessage.getSenderUserId())
//                    .sendEvent("receive_chat_message", savedMessage);
//
//            server.getRoomOperations("user_" + savedMessage.getReceiverUserId())
//                    .sendEvent("receive_chat_message", savedMessage);
//
//        } catch (Exception ex) {
//            client.sendEvent("socket_error", ex.getMessage());
//        }
//    }
//}
//
