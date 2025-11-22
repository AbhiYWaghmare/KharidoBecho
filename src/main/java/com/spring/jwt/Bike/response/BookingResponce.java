package com.spring.jwt.Bike.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponce {
    private String status;
    private String message;
    private Long bookingId;
    private Long bikeId;
    private Long buyerId;
    private LocalDateTime timestamp;

    // constructor
    public BookingResponce(String status, String message, Long bookingId, Long bikeId, Long buyerId) {
        this.status = status;
        this.message = message;
        this.bookingId = bookingId;
        this.bikeId = bikeId;
        this.buyerId = buyerId;
        this.timestamp = LocalDateTime.now();
    }
}
