package com.spring.jwt.Bike.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bike_booking_dto {

    private Long bikeId;

    private Long buyerId;
    private Long userId;

    private String message;
}
