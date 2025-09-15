package com.spring.jwt.laptop.entity;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.User;
import com.spring.jwt.laptop.model.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laptops")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_id")
    private Long laptop_id;

    private String serialNumber;

    private String dealer;

    private String model;

    private String brand;

    private double price;

    @Enumerated(EnumType.STRING)
    private Status Status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

}
