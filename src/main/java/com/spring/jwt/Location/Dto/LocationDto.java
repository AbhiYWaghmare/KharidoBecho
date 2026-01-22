package com.spring.jwt.Location.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private Integer locationId;
    private String state;
    private String city;
    private String locality;
}

