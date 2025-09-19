package com.spring.jwt.Bike.dto;

import com.spring.jwt.Bike.Entity.bikeStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class bikeDto {

    private Long id; // For response or update operations

    @NotNull(message = "Price must not be null")
    private Long prize;

    @NotBlank(message = "Brand must not be blank")
    private String brand;

    @NotBlank(message = "Model must not be blank")
    private String model;

    private String variant;

    @NotNull(message = "Manufacture year is required")
    private Integer manufactureYear;

    private Integer engineCC;

    private Integer kilometersDriven;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    private String color;

    private String registrationNumber;

    private String description;
    @NotNull(message = "Seller ID is required")
    @Column(name = "seller_id")
    private Long sellerId;

    @NotNull(message = "Bike status must not be null")
    private bikeStatus status; // ACTIVE, INACTIVE, DELETED
}
