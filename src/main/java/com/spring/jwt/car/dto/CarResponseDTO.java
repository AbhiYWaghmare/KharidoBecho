package com.spring.jwt.car.dto;

import com.spring.jwt.Mobile.dto.MobileImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponseDTO {
    private Long carId;
    private String title;
    private String description;

    private Boolean airbag;
    private Boolean Abs;
    private Boolean buttonStart;
    private Boolean sunroof;
    private Boolean childSafetyLocks;
    private Boolean acFeature;
    private Boolean musicFeature;

    private BigDecimal price;
    private Boolean negotiable;

    private String condition;
    private String brand;
    private String model;
    private String variant;

    private String color;
    private Integer yearOfPurchase;
    private String fuelType;
    private String transmission;

    private Boolean carInsurance;
    private String carInsuranceDate;
    private String carInsuranceType;

    private Boolean powerWindowFeature;
    private Boolean rearParkingCameraFeature;

    private Integer kmDriven;
    private Integer numberOfOwners;

    private String address;
    private String city;
    private String state;
    private String pincode;

    private String status;
    private Long sellerId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // list of cloudinary image URLs (populated by CarImage module later)
    private List<String> images;
//    private List<String> images;
//    private List<CarImageDTO> images;
}
