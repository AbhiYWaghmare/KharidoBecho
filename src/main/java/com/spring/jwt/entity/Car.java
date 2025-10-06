package com.spring.jwt.entity;

import com.spring.jwt.car.entity.CloudinaryImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private int id;

    @Column(name = "airbag")
    private Boolean airbag;

    @Column(name = "ABS")
    private Boolean ABS;

    @Column(name = "button_start")
    private Boolean buttonStart;

    @Column(name = "sunroof")
    private Boolean sunroof;

    @Column(name = "child_safety_locks")
    private Boolean childSafetyLocks;

    @Column(name = "ac_feature")
    private Boolean acFeature;

    @Column(name = "music_feature")
    private Boolean musicFeature;

    @Column(name = "area", length = 45)
    private String area;

    @Column(name = "variant", length = 45)
    private String variant;

    @Column(name = "brand", nullable = false, length = 45)
    private String brand;

    @Column(name = "car_insurance")
    private Boolean carInsurance;

    @Column(name = "car_insurance_date")
    private String carInsuranceDate;

    @Column(name = "carInsuranceType")
    private String carInsuranceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "carStatus")
    private Status carStatus;

    @Column(name = "pending_approval", nullable = false)
    private boolean pendingApproval;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "color", length = 45)
    private String color;

    @Column(name = "description", length = 5000)
    @Size(max = 5000)
    private String description;

    @Column(name = "fuel_type", length = 45)
    private String fuelType;

    @Column(name = "km_driven")
    private int kmDriven;

    @Column(name = "model", length = 45)
    private String model;

    @Column(name = "owner_serial")
    private int ownerSerial;

    @Column(name = "power_window_feature")
    private Boolean powerWindowFeature;

    @Column(name = "price")
    private int price;

    @Column(name = "rear_parking_camera_feature")
    private Boolean rearParkingCameraFeature;

    @Column(name = "registration", length = 45)
    private String registration;

    @Column(name = "title", length = 250)
    private String title;

    @Column(name = "transmission", length = 45)
    private String transmission;

    @Column(name = "year")
    private int year;

    @Column(name = "date")
    private LocalDate date;

//    @Column(name = "dealer_id")
//    private int dealerId;

    private long carPhotoId;

    @Column(name = "carType", nullable = false)
    private String carType;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CloudinaryImage> images = new HashSet<>();
}
