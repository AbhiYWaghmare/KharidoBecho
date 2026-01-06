package com.spring.jwt.laptop.dto;

import com.spring.jwt.entity.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaptopRequestDTO {

    @NotBlank(message= "Serial number is required")
    @Pattern(
            regexp = "^[A-Za-z0-9\\-]+$",
            message = "Serial number must contain only letters, digits, or hyphens"
    )
    @Size(max = 30, message = "Serial number cannot exceed 30 characters")
    private String serialNumber;

    @NotBlank(message = "Dealer is required")
    @Size(max = 50, message = "Dealer cannot exceed 50 characters")
    @Pattern(
            regexp = "^\\s*(?=.*\\p{L})[\\p{L}\\s\\-\\.]+\\s*$",
            message = "Dealer must contain letters only and cannot have digits or special characters"
    )
    private String dealer;

    @NotBlank(message= "Model is required")
    @Size(max = 50, message = "Model cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Model must contain at least one letter and no special characters")
    private String model;

    @NotBlank(message= "Brand is required")
    @Size(max = 50, message = "Brand cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\s\\-\\.]+$",
            message = "Brand must contain letters only and cannot have digits or special characters")
    private String brand;

    @NotNull(message= "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message= "warranty is required")
    private Long warrantyInYear;

    @Size(max = 50, message = "Processor cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Processor must contain at least one letter and no special characters")
    private String processor;

    @Size(max = 50, message = "Processor Brand cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\s\\-\\.]+$", message = "Processor Brand must contain letters only and cannot have digits or special characters")
    private String processorBrand;

    @Size(max = 50, message = "Memory type cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Memory type must contain at least one letter and no special characters")
    private String memoryType;

    @Size(max = 20, message = "Screen size cannot exceed 20 characters")
    private String screenSize;

    @Size(max = 50, message = "Colour cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Colour must contain at least one letter and no special characters")
    private String colour;

    @NotBlank(message= "RAM is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "RAM must contain letters and digits only and cannot have special characters")
    private String ram;

    @NotBlank(message= "Storage is required")
    @Size(max = 30, message = "Storage cannot exceed 30 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Storage must contains digits and letters only and no special characters")
    private String storage;

    private String battery;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Battery life must contain letters and digits only and cannot have special characters"
    )
    private String batteryLife;

    @Size(max = 30, message = "Graphics card cannot exceed 30 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9\\s\\-\\.]+$",
            message = "Graphics card must contain at least one letter and no special characters")
    private String graphicsCard;

    @Size(max = 50, message = "Graphics brand cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\s\\-\\.]+$",
            message = "Graphics brand must contain letters only and cannot have digits or special characters")
    private String graphicBrand;

    @NotBlank(message = "Weight is required")
    @Pattern(
            regexp = "^(?!0+(\\.0+)?$)([1-9]\\d*(\\.\\d+)?|0?\\.\\d*[1-9]\\d*)\\s?kg$",
            message = "Weight must be a greater than zero"
    )
    private String weight;

    @NotBlank(message= "Manufacturer is required")
    @Size(max = 50, message = "Manufacturer cannot exceed 50 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\s\\-\\.]+$",
            message = "Manufacturer must contain letters only and cannot have digits or special characters")
    private String manufacturer;

    @NotNull(message= "Port is required")
    @Min(value = 1, message = "USB ports must be at least 1")
    private Integer usbPorts;

    private String state;
    private String city;
    private String locality;

    private Status status;

    private LocalDate bookingDate;

    @NotNull(message= "Seller id is required")
    private Long sellerId;

}
