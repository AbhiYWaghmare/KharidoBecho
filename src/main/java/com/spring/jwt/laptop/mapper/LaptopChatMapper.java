//package com.spring.jwt.laptop.mapper;
//
//import com.spring.jwt.laptop.dto.LaptopChatMessageDTO;
//import com.spring.jwt.laptop.entity.LaptopChatMessages;
//
//public class LaptopChatMapper {
//    public static LaptopChatMessageDTO toDTO(
//            LaptopChatMessages msg,
//            String senderRole,
//            Long receiverUserId
//    ) {
//        return LaptopChatMessageDTO.builder()
//                .messageId(msg.getId())
//                .requestId(msg.getRequest().getLaptopBookingId())
//                .senderUserId(msg.getSender().getId())
//                .receiverUserId(receiverUserId)
//                .senderRole(senderRole)
//                .message(msg.getMessage())
//                .createdAt(msg.getCreatedAt())
//                .build();
//    }
//}
