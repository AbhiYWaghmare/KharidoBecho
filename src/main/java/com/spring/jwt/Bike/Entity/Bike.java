package com.spring.jwt.Bike.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bikes")
@Data //
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bike_id;  // Primary key

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

    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BikeImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bike status must not be null")
    private bikeStatus status; // only ACTIVE, INACTIVE, DELETED
}
