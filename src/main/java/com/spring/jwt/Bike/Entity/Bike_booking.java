package com.spring.jwt.Bike.Entity;

import com.spring.jwt.entity.Buyer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bike_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Bike_booking {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "booking_id")
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "bike_id", nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Bike bike;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "buyer_id", nullable = false)
        private Buyer buyer;

//        @Column(name = "message", length = 500)
//        private String message;
//@ElementCollection
//@CollectionTable(
//        name = "booking_conversations",
//        joinColumns = @JoinColumn(name = "booking_id")
//)
//private List<BikeBookingConversation> conversation = new ArrayList<>();
        @Column(name = "conversation", columnDefinition = "JSON")
        private String conversation;


        @Column(name = "booking_date")
        private LocalDate onDate;

        @Enumerated(EnumType.STRING)
        private BookingStatus status;

        private LocalDateTime createdAt;
        @PrePersist
        public void prePersist() {
            this.createdAt = LocalDateTime.now();
            if (this.status == null) this.status = BookingStatus.PENDING;
            if (this.conversation == null) this.conversation = "[]";
        }


        public enum BookingStatus {
            PENDING,
            IN_NEGOTIATION,
            APPROVED,
            ACCEPTED,
            CANCELLED,
            SOLD,
            ACTIVE
        }


    }


