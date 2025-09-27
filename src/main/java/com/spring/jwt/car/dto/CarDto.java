package com.spring.jwt.car.dto;

import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarDto {
    private Integer id;
    private String carName;
    private String brand;
    private String model;
    private String variant;
    private Integer price;
    private String color;
    private String fuelType;
    private Integer kmDriven;
    private String transmission;
    private Integer year;
    private LocalDate date;
    private String description;
    private Integer dealerId;
    private Integer buyerId;
    private String carType;
    private Status carStatus;

    public Integer getCarId() {
        return id;
    }
}
