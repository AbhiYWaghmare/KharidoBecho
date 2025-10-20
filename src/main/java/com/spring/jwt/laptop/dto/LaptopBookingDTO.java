package com.spring.jwt.laptop.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LaptopBookingDTO {

    private String serialNumber;
    private LocalDate bookingDate;

}
