package com.spring.jwt.laptop.entity;

import com.spring.jwt.entity.Buyer;
import com.spring.jwt.laptop.model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "buyer_id",nullable = false)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id",nullable = false)
    private Laptop laptop;

    @Column(name = "booking_date")
    private LocalDate onDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;


}
