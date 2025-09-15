package com.spring.jwt.laptop.dto;

import lombok.Data;

@Data
public class LaptopRequestDTO {

    private String serialNumber;

    private String dealer;

    private String model;

    private String brand;

    private double price;

}
