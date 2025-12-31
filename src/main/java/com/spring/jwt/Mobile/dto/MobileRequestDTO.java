package com.spring.jwt.Mobile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.utils.validation.MaxWords;
import jakarta.validation.constraints.*;
import com.spring.jwt.utils.validation.MinWords;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class MobileRequestDTO {

    //  TITLE
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    // DESCRIPTION
    @NotBlank(message = "Description is required")
//    @Size(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    @MaxWords(value = 70, message = "Description cannot exceed 70 words")
    @MinWords(value = 5, message = "Description must have at least 5 words")
    private String description;

    // PRICE
    @NotNull(message = "Price is required")
    @DecimalMin(value = "1", inclusive = true, message = "Price must be greater than zero")
    @DecimalMax(value = "10000000", inclusive = true, message = "Price cannot exceed 1 crore")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 2 decimal places")
    private BigDecimal price;

    // NEGOTIABLE
    @NotNull(message = "Negotiable must be specified")
    private Boolean negotiable;

    // CONDITION
    @NotBlank(message = "Condition is required")
    @Pattern(regexp = "NEW|USED|REFURBISHED", message = "Condition must be NEW, USED, or REFURBISHED")
    private String condition;

    // BRAND
//    @NotBlank(message = "Brand is required")
//    private String brand;
//
//    //  MODEL
//    @NotBlank(message = "Model is required")
//    private String model;

    @NotNull(message = "Model is required")
    private Long modelId;


    // COLOR
    @NotBlank(message = "Color is required")
    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^[A-Za-z ]{2,30}$",
            message = "Color must be a valid hex code or alphabetic name (2–30 characters)"
    )
    private String color;

    // STATE
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    // CITY
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    // ADDRESS
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;


    // YEAR
    @NotNull(message = "Year of purchase is required")
    @Min(value = 2000, message = "Year must be realistic (after 2000)")
    private Integer yearOfPurchase;

    // SELLER
    @NotNull(message = "SellerId is required")
    private Long sellerId;
}
