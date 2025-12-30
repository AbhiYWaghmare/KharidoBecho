package com.spring.jwt.laptop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import com.spring.jwt.laptop.Dropdown.converter.*;
import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
import com.spring.jwt.laptop.Dropdown.entity.LaptopModel;
import com.spring.jwt.laptop.Dropdown.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "laptops")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laptop_id")
    private Long id;

    @Column(name = "serial_Number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "dealer")
    private String dealer;

    @Column(name = "model")
    private String modelName;

    @Column(name = "brand")
    private String brandName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnore
    private LaptopBrand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    @JsonIgnore
    private LaptopModel model;

    @Column(name = "price")
    private double price;

    @Convert(converter = WarrantyConverter.class)
    @Column(name = "warranty_In_Year")
    private Warranty warrantyInYear;

    @Column(name = "processor")
    private String processor;

    @Convert(converter = ProcessorBrandConverter.class)
    @Column(name = "processor_brand", length = 50)
    private ProcessorBrand processorBrand;

    @Convert(converter = MemoryTypeConverter.class)
    @Column(name = "memory_type", length = 50)
    private MemoryType memoryType;

    @Convert(converter = ScreenSizeConverter.class)
    @Column(name = "screen_size", length = 50)
    private ScreenSize screenSize;

    @Column(name = "colour")
    private String colour;

    @Convert(converter = RamOptionConverter.class)
    @Column(name = "ram", length = 50)
    private RamOption ram;

    @Convert(converter = StorageOptionConverter.class)
    @Column(name = "storage", length = 50)
    private StorageOption storage;

    @Column(name = "battery")
    private String battery;

    @Column(name = "battery_life")
    private String batteryLife;

    @Column(name = "graphics_card")
    private String graphicsCard;

    @Convert(converter = GraphicsBrandConverter.class)
    @Column(name = "graphics_brand", length = 50)
    private GraphicsBrand graphicBrand;


    @Column(name = "weight")
    private String weight;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "usb_port")
    private Integer usbPorts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "address", length = 500)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnore
    private Seller seller;

    @OneToMany(mappedBy = "laptop", fetch = FetchType.LAZY)
    private List<LaptopPhotos> laptopPhotos;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LaptopBooking> bookings = new ArrayList<>();

    private boolean deleted = false;
    private LocalDateTime deletedAt;

    @JsonProperty("brand")
    public String getBrandName() {
        return brand != null ? brand.getBrandName() : null;
    }

    @JsonProperty("model")
    public String getModelName() {
        return model != null ? model.getModelName() : null;
    }

    @JsonProperty("photos")
    public List<String> getPhotoUrls() {
        if (laptopPhotos == null) return List.of();
        return laptopPhotos.stream()
                .map(LaptopPhotos::getPhoto_link)
                .toList();
    }


}
