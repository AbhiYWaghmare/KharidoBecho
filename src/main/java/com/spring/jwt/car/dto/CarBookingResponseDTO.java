package com.spring.jwt.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBookingResponseDTO {
    private String message;

    private Long carId;
    private Long bookingId;
    private String bookingStatus;
    private List<Map<String, Object>> conversation;
}
