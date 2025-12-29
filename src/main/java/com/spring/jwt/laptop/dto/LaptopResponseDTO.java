package com.spring.jwt.laptop.dto;

import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaptopResponseDTO {
        private Long id;
        private String serialNumber;
        private String brand;
        private String model;
        private double price;
        private Long warrantyInYear;
        private String processor;
        private String processorBrand;
        private String memoryType;
        private String screenSize;
        private String colour;
        private String ram;
        private String storage;
        private String battery;
        private String batteryLife;
        private String graphicsCard;
        private String graphicBrand;
        private String weight;
        private String manufacturer;
        private Integer usbPorts;
        private Status status;
        private boolean deleted;
        private LocalDateTime deletedAt;


        private List<LaptopImageDTO> photos;
        private List<LaptopBookingDTO> bookings;
}
