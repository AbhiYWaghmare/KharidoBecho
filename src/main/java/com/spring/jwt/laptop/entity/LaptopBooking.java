package com.spring.jwt.laptop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.spring.jwt.laptop.entity.Laptop;


import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name="laptops_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaptopBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_booking_id")
    private Long laptopBookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laptop_id", nullable = false)
    @JsonIgnoreProperties({"seller", "laptopPhotos"})
    private Laptop laptop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(name = "booking_date")
    private LocalDate onDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LaptopRequestStatus pendingStatus;

    @Column(name = "request_conversation",columnDefinition = "JSON")
    private String requestConversation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        if (this.pendingStatus == null) this.pendingStatus = LaptopRequestStatus.PENDING;
        if (this.requestConversation == null) this.requestConversation = "[]";
    }

}
