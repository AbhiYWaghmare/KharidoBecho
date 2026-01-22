package com.spring.jwt.Location.Controller;

import com.spring.jwt.Location.Dto.LocationDto;
import com.spring.jwt.Location.Service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bikes/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/states")
    public List<String> getStates() {
        return locationService.getAllStates();
    }

    @GetMapping("/cities")
    public List<String> getCities(@RequestParam String state) {
        return locationService.getCitiesByState(state);
    }

    @GetMapping("/localities")
    public List<String> getLocalities(
            @RequestParam String state,
            @RequestParam String city) {
        return locationService.getLocalitiesByStateAndCity(state, city);
    }

    @PostMapping("/add")
    public LocationDto addLocation(@RequestBody LocationDto locationDto) {
        return locationService.saveLocation(locationDto);
    }
}
