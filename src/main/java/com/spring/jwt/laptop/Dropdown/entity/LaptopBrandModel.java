package com.spring.jwt.laptop.Dropdown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "laptop_brand_model",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"brand", "model"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LaptopBrandModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;
}
