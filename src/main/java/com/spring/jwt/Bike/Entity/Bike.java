package com.spring.jwt.Bike.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.jwt.entity.Seller;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "bikes",
        uniqueConstraints = {@UniqueConstraint(columnNames = "registration_number")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long bike_id;  // Primary key

    @NotNull(message = "Prize must not be null")
    @Min(value = 100, message = "Prize must be at least 100")
    private Long prize;  // Price of the bike

    @NotBlank(message = "Brand must not be blank")
    @Size(max = 50, message = "Brand must be at most 50 characters")
    @Size(min = 3, message = "Brand has minimum 3 letter ")
    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "Brand must contain only letters and spaces")
    private String brand;

    @NotBlank(message = "Model must not be blank")
    @Size(max = 50, message = "Model must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Model must contain only letters, numbers, spaces, or hyphens")
    private String model;

    @NotBlank(message = "Varient must not be blank")
    @Size(max = 50, message = "Variant must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "Variant must contain only letters, numbers, spaces, or hyphens")
    private String variant;

    @NotNull(message = "Manufacture year is required")
    @Digits(integer = 4, fraction = 0, message = "Manufacture year must be a whole number (no decimals)")
    @Min(value = 1900, message = "Manufacture year must be after 1900")
    private Integer manufactureYear;

    @Min(value = 50, message = "Engine CC must be at least 50")
    @Max(value = 3000, message = "Engine CC must be at most 3000")
    private Integer engineCC;

    @Min(value = 0, message = "Kilometers driven must be greater than zero")
    private Integer kilometersDriven;

    @NotNull(message = "Fuel type must not be blank")
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;


    @NotBlank(message = "Colour must not be blank ")
    @Size(max = 20, message = "Color must be at most 20 characters")
    @Size(min = 3, message = "Colour has minimum 3 letter ")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Color must contain only letters and spaces")
    private String color;

    @Column(unique = true)
    @NotBlank(message = "Registration number must not be blank")
    @Pattern(regexp = "^[A-Z0-9\\s]+$", message = "registration number must contain only letters and number")
    private String registrationNumber;

    @NotBlank(message ="Description Must not be Blank")
    @Size(max = 500, message = "Description must be at most 500 characters")
    @Size(min = 10,message = "Description have at least 10 characters ")
    private String description;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    //@JsonIgnore
    private Seller seller;

@OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
@JsonManagedReference
private List<BikeImage> images = new ArrayList<>();

    @NotNull(message = "Bike status must not be null")
    @Enumerated(EnumType.STRING)
    private bikeStatus status; // only ACTIVE, INACTIVE, DELETED
}
