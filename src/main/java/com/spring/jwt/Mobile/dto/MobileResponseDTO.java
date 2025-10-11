package com.spring.jwt.Mobile.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class


MobileResponseDTO {
    private Long mobileId;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean negotiable;
    private String condition;
    private String brand;
    private String model;
    private String color;
    private Integer yearOfPurchase;
    private String status; //ACTIVE, SOLD, EXPIRED, DELETED
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long sellerId;
    private List<String> images; // list of image URLs
}
