//package com.spring.jwt.laptop.controller;
//
//import com.spring.jwt.laptop.dto.LaptopChatMessageDTO;
//import com.spring.jwt.laptop.service.impl.LaptopChatServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/laptop/chat")
//@RequiredArgsConstructor
//public class LaptopChatController {
//    private final LaptopChatServiceImpl chatService;
//
//    @GetMapping("/{bookingId}/message")
//    public ResponseEntity<List<LaptopChatMessageDTO>> getChat(
//            @PathVariable Long bookingId) {
//
//        return ResponseEntity.ok(chatService.getChat(bookingId));
//    }
//
//}
