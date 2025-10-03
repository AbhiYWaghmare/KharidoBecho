package com.spring.jwt.laptop.dto;

import com.spring.jwt.laptop.model.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LaptopRequestDTO {

    @NotBlank(message= "Serial number is required")
    private String serialNumber;
    @NotBlank(message= "Dealer is required")
    private String dealer;
    @NotBlank(message= "Model is required")
    private String model;
    @NotBlank(message= "Brand is required")
    private String brand;
    @NotNull(message= "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

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

    @NotNull(message= "Seller id is required")
    private Long sellerId;
}
