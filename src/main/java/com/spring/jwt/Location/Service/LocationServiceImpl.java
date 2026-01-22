package com.spring.jwt.Location.Service;

import com.spring.jwt.Location.Dto.LocationDto;
import com.spring.jwt.Location.Entity.LocationMaster;
import com.spring.jwt.Location.Repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<String> getAllStates() {
        return locationRepository.findDistinctStates();
    }

    @Override
    public List<String> getCitiesByState(String state) {
        return locationRepository.findCitiesByState(state);
    }

    @Override
    public List<String> getLocalitiesByStateAndCity(String state, String city) {
        return locationRepository.findLocalitiesByStateAndCity(state, city);
    }

    @Override
    public LocationDto saveLocation(LocationDto locationDTO) {
        LocationMaster location = new LocationMaster();
        location.setState(locationDTO.getState());
        location.setCity(locationDTO.getCity());
        location.setLocality(locationDTO.getLocality());

        LocationMaster saved = locationRepository.save(location);

        return new LocationDto(
                saved.getLocationId(),
                saved.getState(),
                saved.getCity(),
                saved.getLocality()
        );
    }
}
