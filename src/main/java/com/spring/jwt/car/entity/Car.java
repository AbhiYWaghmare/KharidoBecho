package com.spring.jwt.car.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    // TITLE
    @Column(nullable = false, length = 150)
    private String title;

    // DESCRIPTION
    @Column(nullable = false, length = 2000)
    private String description;

    //AIRBAG
    @Column(name = "airbag")
    private Boolean airbag;

    //ABS
    @Column(name = "ABS")
    private Boolean abs;

    //BUTTONSTART
    @Column(name = "button_start")
    private Boolean buttonStart;

    //SUNROOF
    @Column(name = "sunroof")
    private Boolean sunroof;

    @Column(name = "registration_number", length = 20, unique = true)
    private String registrationNumber;


    //CHILD SAFETY LOCK
    @Column(name = "child_safety_locks")
    private Boolean childSafetyLocks;

    //AC FEACTURE
    @Column(name = "ac_feature")
    private Boolean acFeature;

    //MUSIC FEACTURE
    @Column(name = "music_feature")
    private Boolean musicFeature;

    // PRICE
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;


    // NEGOTIABLE
    private Boolean negotiable;

    // CONDITION
    @Enumerated(EnumType.STRING)
    @Column(name = "car_condition",nullable = false)
    private Condition condition;

    public enum Condition {
        NEW, USED, REFURBISHED
    }

    // BRAND & MODEL
    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "variant", length = 45)
    private String variant;

    // COLOR
    @Column(nullable = false, length = 30)
    private String color;

    // YEAR OF PURCHASE
    private Integer yearOfPurchase;

    // FUEL TYPE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    public enum FuelType {
        PETROL, DIESEL, ELECTRIC, HYBRID
    }

    //CAR INSURENCE

    @Column(name = "car_insurance")
    private Boolean carInsurance;

    @Column(name = "car_insurance_date")
    private String carInsuranceDate;

    @Column(name = "carInsuranceType")
    private String carInsuranceType;

    // TRANSMISSION
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transmission transmission;

    public enum Transmission {
        MANUAL, AUTOMATIC
    }

    //POWER WINDOW
    @Column(name = "power_window_feature")
    private Boolean powerWindowFeature;

    //REAR PARKING CAMERA
    @Column(name = "rear_parking_camera_feature")
    private Boolean rearParkingCameraFeature;

    // KM DRIVEN
    private Integer kmDriven;

    // NUMBER OF OWNERS
    private Integer numberOfOwners;

    // ADDRESS
    @Column(nullable = false, length = 255)
    private String address;
    private String city;
    private String state;

    @Column(length = 6)
    private String pincode;


    // STATUS + SOFT DELETE
    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Status {
        ACTIVE, SOLD, DELETED
    }

    private boolean deleted = false;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;


    // SELLER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Seller seller;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CarImage> images;

}
