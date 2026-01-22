package com.spring.jwt.car.carbranddata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarBrandData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandDataId;

    private String brand;
    private String variant;
    private String subVariant;
}
