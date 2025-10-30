package com.spring.jwt.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBookingResponseDTO {
//    private String code;
    private String message;
//    private Integer statusCode;
//    private LocalDateTime timeStamp;
//    private String exception;
//    private String apiPath;
    private Long carId;
    private Long bookingId;
    private String bookingStatus;
}
