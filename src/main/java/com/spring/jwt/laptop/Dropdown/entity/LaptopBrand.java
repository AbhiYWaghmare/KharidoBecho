package com.spring.jwt.laptop.Dropdown.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laptop_brands", uniqueConstraints = {
        @UniqueConstraint(columnNames = "brand_name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name", nullable = false, length = 100)
    private String brandName;


}
