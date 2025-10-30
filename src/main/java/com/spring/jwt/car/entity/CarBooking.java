//package com.spring.jwt.car.entity;
//
//import com.spring.jwt.entity.Status;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "car_bookings")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class CarBooking {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "booking_id")
//    private Long id;
//
////    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "car_id", nullable = false)
//    @ManyToOne
//    private Car car;
//
//    @Column(name = "booking_date")
//    private LocalDate onDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private Status status;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//}
package com.spring.jwt.car.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.entity.Buyer;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "car_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Prevents ByteBuddy proxy error
public class CarBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Buyer buyer;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private Status bookingStatus;

    @Column(name = "booking_date", nullable = false)
    private OffsetDateTime bookingDate;

    @PrePersist
    private void prePersist() {
        if (this.bookingDate == null) {
            this.bookingDate = OffsetDateTime.now();
        }
        if (this.bookingStatus == null) {
            this.bookingStatus = Status.PENDING;
        }
    }

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        ACTIVE,
        SOLD
    }
}

