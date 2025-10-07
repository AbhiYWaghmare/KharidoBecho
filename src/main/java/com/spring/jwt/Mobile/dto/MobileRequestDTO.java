package com.spring.jwt.Mobile.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Year;

@Data
public class MobileRequestDTO {

    //Title
    @NotBlank(message = "Title is required")
    private String title;

    //Description
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    //Price
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero")
    @DecimalMax(value = "10000000", inclusive = true, message = "Price cannot exceed 1 crore")
    private BigDecimal price;

    //Negotiable
//    @NotNull(message = "Negotiable field is required")
    private Boolean negotiable;

    //Condition
    @NotBlank(message = "Condition is required")
    @Pattern(regexp = "NEW|USED|REFURBISHED", message = "Condition must be NEW, USED or REFURBISHED")
    private String condition;

    //Brand
    @NotBlank(message = "Brand is required")
    private String brand;

    // Model
    @NotBlank(message = "Model is required")
    private String model;

    //Color
    @NotBlank(message = "Color is required")
    private String color;

    //Year of Purchase
    @NotNull(message = "Year of purchase is required")
    @Max(value = 2030, message = "Year cannot be in the future") // you can also dynamically set to current year
    @Min(value = 2000, message = "Year must be realistic") // optional lower bound
    private Integer yearOfPurchase;

    // Seller ID
    @NotNull(message = "SellerId is required")
    private Long sellerId;

    // validate that year is not greater than current year
    public boolean isYearValid() {
        return yearOfPurchase == null || yearOfPurchase <= Year.now().getValue();
    }
}
