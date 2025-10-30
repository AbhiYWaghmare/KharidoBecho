package com.spring.jwt.Bike.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FuelType {
    PETROL,
    EV;

    @JsonCreator
    public static FuelType from(String value) {
        return FuelType.valueOf(value.toUpperCase());
    }
}
