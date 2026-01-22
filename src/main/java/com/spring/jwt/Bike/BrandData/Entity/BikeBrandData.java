package com.spring.jwt.Bike.BrandData.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "Bike_Brand_Data", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"brand","Model","Varient" })
})

public class BikeBrandData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brandDataId", nullable = false)
    private Integer brandDataId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "Model")
    private String model;

    @Column(name = "variant")
    private String variant;

    @Column(name = "enginecc")
    private Integer engineCC;

    public BikeBrandData() {

    }
}
