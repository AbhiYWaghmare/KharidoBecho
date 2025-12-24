package com.spring.jwt.laptop.dto;

import lombok.Data;

@Data
public class LaptopBookingDTO {
    private Long laptopBookingId;
    private Long buyerId;
    private Long sellerId;
    private String status;
}
