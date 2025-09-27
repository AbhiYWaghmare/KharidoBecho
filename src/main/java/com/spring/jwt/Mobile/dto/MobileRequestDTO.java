package com.spring.jwt.Mobile.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MobileRequestDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean negotiable;
    private String condition; // NEW / USED / REFURBISHED
    private String brand;
    private String model;
    private String color;
    private Integer yearOfPurchase;
    private Long sellerId; // If you are using authentication then no need to pass sellerID
}
