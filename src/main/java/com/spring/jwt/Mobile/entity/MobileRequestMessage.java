//package com.spring.jwt.Mobile.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.OffsetDateTime;
//
//@Entity
//@Table(name = "mobile_request_messages")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class MobileRequestMessage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long messageId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "request_id", nullable = false)
//    private MobileRequest request;
//
//    @Column(nullable = false)
//    private Long senderId; // buyer or seller id
//
//    @Column(length = 2000)
//    private String message;
//
//    @Column(name = "sent_at", nullable = false, updatable = false)
//    private OffsetDateTime sentAt;
//
//    @PrePersist
//    private void prePersist() {
//        this.sentAt = OffsetDateTime.now();
//    }
//}
