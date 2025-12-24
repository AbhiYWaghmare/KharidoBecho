package com.spring.jwt.car.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.car.converter.ConversationConverter;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "car_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CarBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Buyer buyer;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private Status bookingStatus;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Seller seller;

    @Column(name = "booking_date", nullable = false)
    private OffsetDateTime bookingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ✅ CORRECT TYPE — LIST, NOT STRING
    @Convert(converter = ConversationConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> conversation = new ArrayList<>();
//    @ElementCollection
//    @CollectionTable(name = "car_booking_conversation", joinColumns = @JoinColumn(name = "booking_id"))
//    @Column(name = "message")
//    private List<Map<String, Object>> conversation = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.bookingStatus == null) this.bookingStatus = Status.PENDING;
        if (this.conversation == null) this.conversation = new ArrayList<>();
    }

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        ACTIVE,
        SOLD,
        REJECTED
    }
}
