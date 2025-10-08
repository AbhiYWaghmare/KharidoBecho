package com.spring.jwt.car.dto;

import com.spring.jwt.entity.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarDto {
    private Long carId;
    private String carName;
    private String brand;
    private String model;
    private String variant;
    @Positive(message = "Price must be a positive number")
    private Integer price;
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Color must contain only letters")
    private String color;
    @Pattern(regexp = "^(Petrol|Diesel|CNG|Electric)$", message = "Fuel type must be Petrol, Diesel, CNG, or Electric")
    @NotNull(message = "Fuel type is required")
    private String fuelType;
    private Integer kmDriven;
    private String transmission;

    @Min(value = 1886, message = "Year must be no earlier than 1886")
    @Max(value = 2026, message = "Year cannot be in the future")
    private Integer year;
    private LocalDate date;
    private String description;
    private Integer dealerId;
    private Integer buyerId;
    private String carType;
    @NotNull(message = "Car status is required")
    private Status carStatus;
    private String registration;

    @NotNull(message="Seller id is required")
    private Long sellerId;
    private Long carPhotoId;

    private String city;
    private String area;
    private Boolean abs;
    private Boolean acFeature;
    private Boolean airbag;
    private Boolean buttonStart;
    private Boolean childSafetyLocks;
    private Boolean musicFeature;
    private Boolean powerWindowFeature;
    private Boolean rearParkingCameraFeature;
    private Boolean sunroof;

    private String carInsurance;          // e.g. insurance number or provider
    private String carInsuranceType;      // e.g. third party / comprehensive
    private LocalDate carInsuranceDate;   // insurance expiry or issue date

    private Boolean pendingApproval;



}
