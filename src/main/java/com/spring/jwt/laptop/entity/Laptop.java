package com.spring.jwt.laptop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.User;
import com.spring.jwt.laptop.model.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "laptops")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_id")
    private Long id;

    @NotBlank
    @Column(name = "serial_Number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "dealer")
    private String dealer;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "price")
    private double price;

    @Column(name = "warranty_In_Year")
    private Long warrantyInYear;

    @Column(name = "processor")
    private String processor;

    @Column(name = "processor_brand")
    private String processorBrand;

    @Column(name = "memory_type")
    private String memoryType;

    @Column(name = "screen_size")
    private String screenSize;

    @Column(name = "colour")
    private String colour;

    @Column(name = "ram")
    private String ram;

    @Column(name = "storage")
    private String storage;

    @Column(name = "battery")
    private String battery;

    @Column(name = "battery_life")
    private String batteryLife;

    @Column(name = "graphics_card")
    private String graphicsCard;

    @Column(name = "graphics_brand")
    private String graphicBrand;

    @Column(name = "weight")
    private String weight;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "usb_port")
    private Integer usbPorts;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnore
    private Seller seller;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LaptopPhotos> laptopPhotos = new ArrayList<>();

    private boolean deleted = false;

    private LocalDateTime deletedAt;

}
