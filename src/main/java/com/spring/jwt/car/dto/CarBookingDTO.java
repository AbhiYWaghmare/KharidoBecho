             package com.spring.jwt.car.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class CarBookingDTO {
    private Long carId;
    private Long buyerId;
    private Long userId;

    private String message;

//    private OffsetDateTime bookingDate;
}
