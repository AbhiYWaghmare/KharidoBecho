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

    @NotNull(message= "warranty is required")
    private Long warrantyInYear;
    @NotBlank(message= "Processor is required")
    private String processor;
    @NotBlank(message= "Processor Brand is required")
    private String processorBrand;
    @NotBlank(message= "Memory type is required")
    private String memoryType;
    @NotBlank(message= "Screen size is required")
    private String screenSize;
    @NotBlank(message= "Colour is required")
    private String colour;
    @NotBlank(message= "RAM is required")
    private String ram;
    @NotBlank(message= "Storage is required")
    private String storage;
    @NotBlank(message= "Battery is required")
    private String battery;
    @NotBlank(message= "Battery Life is required")
    private String batteryLife;
    @NotBlank(message= "Graphics card is required")
    private String graphicsCard;
    @NotBlank(message= "Graphics brand is required")
    private String graphicBrand;
    @NotBlank(message= "Weight is required")
    private String weight;
    @NotBlank(message= "Manufacturer is required")
    private String manufacturer;
    @NotNull(message= "Port is required")
    private Integer usbPorts;

    private Status status;


    @NotNull(message= "Seller id is required")
    private Long sellerId;
}
