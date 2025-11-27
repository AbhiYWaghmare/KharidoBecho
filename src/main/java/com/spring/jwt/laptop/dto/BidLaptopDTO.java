package com.spring.jwt.laptop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BidLaptopDTO {
    private Long beadingLaptopId;

//    private Long laptopId;

    private Long buyerId;

    private Double bidAmount;

    private  String Message;


}
