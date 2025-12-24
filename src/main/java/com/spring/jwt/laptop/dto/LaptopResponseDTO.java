package com.spring.jwt.laptop.dto;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaptopResponseDTO {
//    private String status;
//    private String message;
//    private String code;
//    private Integer statusCode;
//    private LocalDateTime timeStamp;
//    private String exception;
//    private String apiPath;
//    private String imageUrl;
//    private Long laptopId;
////    private Long bookingId;

        private Long id;
        private String serialNumber;
        private String brand;
        private String model;
        private double price;
        private Status status;
//        private Seller seller;

        private List<LaptopImageDTO> photos;
        private List<LaptopBookingDTO> bookings;
}
