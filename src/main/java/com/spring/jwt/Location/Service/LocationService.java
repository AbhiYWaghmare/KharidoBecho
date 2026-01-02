package com.spring.jwt.Location.Service;

import com.spring.jwt.Location.Dto.LocationDto;

import java.util.List;

public interface LocationService {

    List<String> getAllStates();

    List<String> getCitiesByState(String state);

    List<String> getLocalitiesByStateAndCity(String state, String city);

    LocationDto saveLocation(LocationDto locationDTO);
}
