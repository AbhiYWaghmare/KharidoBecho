//package com.spring.jwt.laptop.laptopAuction.laptopSocket.handler;
//
//import com.corundumstudio.socketio.SocketIOServer;
//import com.spring.jwt.laptop.dto.LaptopRequestResponseDTO;
////import com.spring.jwt.laptop.laptopAuction.laptopSocket.dto.LaptopChatMessageDTO;
//import com.spring.jwt.laptop.laptopAuction.laptopSocket.dto.LaptopChatMessageDTO;

//import com.spring.jwt.laptop.service.LaptopRequestService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class ChatSocketHandler {
//
//    public ChatSocketHandler(SocketIOServer server,
//                             LaptopRequestService laptopRequestService){
//
//        server.addEventListener("join_chat", Long.class,(client,requestId,ack) -> {
//            client.joinRoom("chat_" +requestId);
//            log.info("Client {} joined chat room {}", client.getSessionId(),requestId);
//        });
//
//        server.addEventListener("send_chat", LaptopChatMessageDTO.class, (client, chat, ack) -> {
//
//            Long requestId = chat.getRequestId();
//            Long senderId = chat.getUserId();
//            String message = chat.getMessage();
//
//            log.info("New Chat: request={}, user={}, msg={}", requestId, senderId, message);
//
//            // -------------------------------------------
//            // Validate request exists
//            // -------------------------------------------
//            LaptopRequestResponseDTO existingRequest = laptopRequestService.getRequestById(requestId);
//            if (existingRequest == null) {
//                log.error("Chat failed: Request {} not found", requestId);
//                client.sendEvent("chat_error", "Request ID " + requestId + " does not exist.");
//                return; // STOP execution → do not broadcast
//            }
//
//            // -------------------------------------------
//            // Validate user exists
//            // -------------------------------------------
//            if (!laptopRequestService.userExists(senderId)) {
//                log.error(" Chat failed: User {} not found", senderId);
//                client.sendEvent("chat_error", "User ID " + senderId + " does not exist.");
//                return;
//            }
//
//            // Validate user belongs to this request (buyer or seller)
//            boolean allowed =
//                    senderId.equals(existingRequest.getBuyerId()) ||
//                            senderId.equals(existingRequest.getSellerId());
//
//// Backward compatibility: allow users that already appear in conversation history
//            if (!allowed && existingRequest.getConversation() != null) {
//                allowed = existingRequest.getConversation().stream()
//                        .anyMatch(c -> c.getSenderId().equals(senderId));
//            }
//
//            if (!allowed) {
//                log.error("Chat failed: User {} is not part of request {}", senderId, requestId);
//                client.sendEvent("chat_error", "You are not allowed to chat in this request.");
//                return;
//            }
//
//
//            // -------------------------------------------
//            // Save chat and broadcast
//            // -------------------------------------------
//            LaptopRequestResponseDTO updated =
//                    laptopRequestService.appendMessage(requestId, senderId, message);
//
//            server.getRoomOperations("chat_" + requestId)
//                    .sendEvent("chat_update", updated);
//        });
//    }
//}
