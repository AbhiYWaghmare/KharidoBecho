package com.spring.jwt.car.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.utils.validation.MaxWords;
import com.spring.jwt.utils.validation.MinWords;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class CarRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    @MaxWords(value = 70, message = "Description cannot exceed 70 words")
    @MinWords(value = 5, message = "Description must have at least 5 words")
    private String description;

    @NotNull(message = "Airbag information must be provided")
    private Boolean airbag;

    @NotNull(message = "ABS information must be provided")
    private Boolean abs;

    @NotNull(message = "Button start information must be provided")
    private Boolean buttonStart;

    @NotNull(message = "Sunroof information must be provided")
    private Boolean sunroof;

    @NotNull(message = "Child safety locks information must be provided")
    private Boolean childSafetyLocks;

    @NotNull(message = "AC feature information must be provided")
    private Boolean acFeature;

    @NotNull(message = "Music feature information must be provided")
    private Boolean musicFeature;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "1", message = "Price must be greater than zero")
    @DecimalMax(value = "10000000", message = "Price cannot exceed 1 crore")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Negotiable must be specified")
    private Boolean negotiable;

    @NotBlank(message = "Condition is required")
    @Pattern(regexp = "NEW|USED|REFURBISHED", message = "Condition must be NEW, USED, or REFURBISHED")
    private String condition;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Variant is required")
    @Size(max = 45, message = "Variant length must be <= 45")
    private String variant;

    @NotBlank(message = "Color is required")
    @Size(max = 30, message = "Color length must be <= 30")
    private String color;

    @NotNull(message = "Year of purchase is required")
    @Min(value = 2000, message = "Year must be realistic (after 2000)")
    private Integer yearOfPurchase;

    @NotBlank(message = "Fuel type is required")
    @Pattern(regexp = "PETROL|DIESEL|ELECTRIC|HYBRID", message = "Fuel type must be PETROL, DIESEL, ELECTRIC, or HYBRID")
    private String fuelType;

    @NotNull(message = "Car insurance information must be provided")
    private Boolean carInsurance;

    @NotBlank(message = "Car insurance date must be provided")
    private String carInsuranceDate;

    @NotBlank(message = "Car insurance type must be provided")
    private String carInsuranceType;

    @NotBlank(message = "Transmission is required")
    @Pattern(regexp = "MANUAL|AUTOMATIC", message = "Transmission must be MANUAL or AUTOMATIC")
    private String transmission;

    @NotNull(message = "Power window feature information must be provided")
    private Boolean powerWindowFeature;

    @NotNull(message = "Rear parking camera information must be provided")
    private Boolean rearParkingCameraFeature;

    @NotNull(message = "Kilometers driven must be provided")
    @Min(value = 0, message = "Kilometers driven cannot be negative")
    private Integer kmDriven;

    @NotNull(message = "Number of owners must be provided")
    @Min(value = 1, message = "Number of owners must be at least 1")
    @Max(value = 10, message = "Number of owners cannot exceed 10")
    private Integer numberOfOwners;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address max length is 255")
    private String address;

    @NotBlank(message = "City must be provided")
    private String city;

    @NotBlank(message = "State must be provided")
    private String state;

    @NotBlank(message = "Pincode must be provided")
    @Size(max = 6, message = "Pincode max length is 6")
    private String pincode;

    @NotNull(message = "SellerId is required")
    private Long sellerId;
}

