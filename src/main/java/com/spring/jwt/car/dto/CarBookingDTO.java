package com.spring.jwt.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBookingDTO {
    private Long carId;
    private Long buyerId;
//    private OffsetDateTime bookingDate;
}
