package com.spring.jwt.laptop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LaptopRequestDTO {

    @NotBlank
    private String serialNumber;

    @NotBlank
    private String dealer;

    private String model;

    private String brand;

    private double price;

}
