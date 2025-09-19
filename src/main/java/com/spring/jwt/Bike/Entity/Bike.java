package com.spring.jwt.Bike.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "bikes")
@Data //
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key

    @NotNull(message = "Prize must not be null")
    private Long prize;  // Price of the bike

    @NotNull(message = "Brand must not be null")
    private String brand;

    @NotNull(message = "Model must not be null")
    private String model;

    private String variant;

    @NotNull(message = "Manufacture year is required")
    private Integer manufactureYear;

    private Integer engineCC;

    private Integer kilometersDriven;

    @NotNull(message = "Fuel type must not be null")
    private String fuelType;

    private String color;

    @Column(unique = true)
    private String registrationNumber;

    private String description;

    @NotNull(message = "Seller ID is required")
    @Column(name = "seller_id")

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bike status must not be null")
    private bikeStatus status; // only ACTIVE, INACTIVE, DELETED
}
