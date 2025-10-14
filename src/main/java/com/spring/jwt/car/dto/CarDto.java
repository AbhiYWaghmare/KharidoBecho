package com.spring.jwt.car.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.spring.jwt.entity.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarDto {

    @JsonProperty(access = Access.READ_ONLY)
    private Long carId;

    @NotBlank(message = "Car name is required")
    @Pattern(regexp = "^[A-Za-z0-9 ]{2,50}$", message = "Car name must be alphanumeric and 2–50 characters long")
    private String carName;

    @NotBlank(message = "Brand is required")
    @Pattern(regexp = "^[A-Za-z ]{2,30}$", message = "Brand must only contain letters and spaces")
    private String brand;

    @NotBlank(message = "Model is required")
    @Pattern(regexp = "^[A-Za-z0-9 ]{1,30}$", message = "Model must be alphanumeric and 1–30 characters long")
    private String model;

    @NotBlank(message = "Variant is required")
    private String variant;

    @NotNull(message = "Price is mandatory")
    @Min(value = 1000, message = "Price must be at least 1000")
    @Max(value = 99999999, message = "Price cannot exceed 99999999")
    private Integer price;

    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Color must only contain letters")
    private String color;

    @NotBlank(message = "Fuel type is required")
    @Pattern(regexp = "^(Petrol|Diesel|CNG|Electric|Hybrid)$", message = "Fuel type must be one of: Petrol, Diesel, CNG, Electric, Hybrid")
    private String fuelType;

    @NotBlank(message = "Transmission is required")
    @Pattern(regexp = "^(Manual|Automatic|CVT)$", message = "Transmission must be one of: Manual, Automatic, CVT")
    private String transmission;

    @NotNull(message = "Year is required")
    @Min(value = 1886, message = "Year must be no earlier than 1886")
    @Max(value = 2026, message = "Year cannot be in the future")
    private Integer year;

    @NotBlank(message = "Registration number is required")
    @Pattern(regexp = "^[A-Z]{2} ?\\d{1,2} ?[A-Z]{1,2} ?\\d{4}$", message = "Registration must be in format 'MH 12 AB 1234'")
    private String registration;

    public void setRegistration(String registration) {
        if (registration != null) {
            this.registration = registration.trim().toUpperCase();
        }
    }

    @NotBlank(message = "Area is mandatory")
    @Size(min = 2, max = 50, message = "Area must be between 2 and 50 characters")
    private String area;

    @NotBlank(message = "City is mandatory")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "City must contain only letters and spaces")
    private String city;

    @PositiveOrZero(message = "KM Driven cannot be negative")
    @Max(value = 1000000, message = "KM Driven seems unrealistic")
    private Integer kmDriven;

    @NotNull(message = "Insurance status is required")
    private String carInsurance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate carInsuranceDate;

    @Pattern(regexp = "^(Comprehensive|Third Party|None)?$", message = "Insurance type must be Comprehensive, Third Party, or None")
    private String carInsuranceType;

    @NotBlank(message = "Car type is required")
    @Pattern(regexp = "^(Sedan|Hatchback|SUV|MUV|Coupe)$", message = "Car type must be one of: Sedan, Hatchback, SUV, MUV, Coupe")
    private String carType;

    @NotNull(message = "Seller id is required")
    private Long sellerId;

    @NotNull(message = "Car status is required")
    private Status carStatus;

    private Boolean abs;
    private Boolean airbag;
    @JsonProperty("acFeature")
    private Boolean acFeature;
    private Boolean buttonStart;
    private Boolean childSafetyLocks;
    private Boolean sunroof;
    private Boolean musicFeature;

    private Boolean powerWindowFeature;

    private Boolean rearParkingCameraFeature;
    private Boolean pendingApproval;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;
}
