package com.spring.jwt.Bike.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.entity.Seller;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class bikeDto {

    private Long bike_id;

    @NotNull(message = "Prize must not be null")
    @Min(value = 100, message = "Prize must be at least 100")
    @Max(value = 3000000, message = "Prize must not be greater than 3000000")
    private Long prize;

    @NotBlank(message = "Brand must not be blank")
    @Size(max = 50, message = "Brand must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Brand must contain only letters and spaces")
    private String brand;

    @NotBlank(message = "Model must not be blank")
    @Size(max = 50, message = "Model must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Model must contain only letters, numbers, spaces, or hyphens")
    private String model;

    @Size(max = 50, message = "Variant must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "Variant must contain only letters, numbers, spaces, or hyphens")
    private String variant;

    @NotNull(message = "Manufacture year is required")
    @Min(value = 1900, message = "Manufacture year must be after 1900")
    @Max(value = 2100, message = "Manufacture year must be before 2100")
    private Integer manufactureYear;

    @Min(value = 50, message = "Engine CC must be at least 50")
    @Max(value = 3000, message = "Engine CC must be at most 3000")
    private Integer engineCC;

    @Min(value = 0, message = "Kilometers driven must be >= 0")
    private Integer kilometersDriven;

    @NotBlank(message = "Fuel type must not be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Fuel type must contain only letters and spaces")
    private String fuelType;

    @Size(max = 20, message = "Color must be at most 20 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Color must contain only letters and spaces")
    private String color;

    @NotBlank(message = "Registration number must not be blank")
    @Pattern(regexp = "^[A-Z0-9\\s]+$", message = "Registration number must contain only letters and numbers")
    private String registrationNumber;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;


    @Column(name = "seller_id")
    @Min(value = 1, message = "Seller ID must be positive")
    private Long sellerId;

    @NotNull(message = "Bike status must not be null")
    private bikeStatus status;
}
