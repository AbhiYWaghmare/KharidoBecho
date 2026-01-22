package com.spring.jwt.laptop.Dropdown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laptop_models",
        uniqueConstraints = @UniqueConstraint(columnNames = {"model_name", "brand_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @Column(nullable = false, length = 100)
    private String modelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private LaptopBrand brand;
}
