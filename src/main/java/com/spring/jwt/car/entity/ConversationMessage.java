//package com.spring.jwt.car.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.OffsetDateTime;
//
//@Entity
//@Table(name = "car_conversation_messages")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Embeddable
//public class ConversationMessage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long senderId;          // Buyer किंवा Seller
//    private String senderType;      // "BUYER" किंवा "SELLER"
//    private String message;
//
//    private OffsetDateTime timestamp = OffsetDateTime.now();
//
////    // प्रत्येक message एका booking शी जोडलेला असेल
////    @ManyToOne
////    @JoinColumn(name = "booking_id")
////    private CarBooking booking;
//
//}