//package com.spring.jwt.laptop.entity;
//
//import com.spring.jwt.entity.User;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.OffsetDateTime;
//
//@Entity
//@Getter @Setter
//@Table(name = "laptop_chat_messages")
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class LaptopChatMessages {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "laptop_request_id", nullable = false)
//    private LaptopBooking request;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_user_id", nullable = false)
//    private User sender;
//
//    @Column(nullable = false, length = 1000)
//    private String message;
//
//    @Column(nullable = false)
//    private OffsetDateTime createdAt;
//
//    @PrePersist
//    public void onCreate() {
//        this.createdAt = OffsetDateTime.now();
//    }
//
//}
