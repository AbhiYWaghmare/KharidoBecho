package com.spring.jwt.car.carbranddata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(
        name = "BrandData",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"brand", "variant", "subVariant"})
        }
)
public class BrandData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandDataId;

    private String brand;
    private String variant;
    private String subVariant;

//    private String fuelType;        // Petrol, Diesel, CNG, EV
//    private String transmission;    // Manual, Automatic
//    private String bodyType;         // Hatchback, Sedan, SUV
//    private Integer engineCC;
//    private Integer yearFrom;
//    private Integer yearTo;

    public BrandData() {}
}
