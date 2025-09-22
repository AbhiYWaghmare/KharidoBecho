package com.spring.jwt.car.dto;

import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarDto {
    private Integer id;                 // --- NEW/Important: Primary key of car
    private String carName;             // --- maps to Car.title
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
    private Integer dealerId;          // seller ID (maps to dealer)
    private Integer buyerId;            // --- NEW: buyer ID (optional, maps to ownerSerial)
    private String mainCarId;           // --- NEW: unique identifier for car
    private String carType;
    private Status carStatus;           // --- NEW: ACTIVE / DELETED / INACTIVE etc.

    public Integer getCarId() {
       return id;
    }
}
