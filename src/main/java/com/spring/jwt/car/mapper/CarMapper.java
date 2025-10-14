package com.spring.jwt.car.mapper;

import com.spring.jwt.car.dto.CarDto;
import com.spring.jwt.entity.Car;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    private final ModelMapper mapper;

    public CarMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @PostConstruct
    public void setup() {
        // 1️⃣ Map Car → CarDto
        mapper.addMappings(new PropertyMap<Car, CarDto>() {
            @Override
            protected void configure() {
                map().setCarName(source.getTitle());
//                map().setCarId(source.getCarId()); // ✅ Long → Long
//                map().setCarId(source.getCarId());
                // sellerId handled in service if needed
            }
        });

        // 2️⃣ Map CarDto → Car
        mapper.addMappings(new PropertyMap<CarDto, Car>() {
            @Override
            protected void configure() {
                map().setTitle(source.getCarName());
//                map().setCarId(source.getCarId()); // ✅ Long → Long
                // sellerId handled manually
            }
        });
    }

    public CarDto toDto(Car car) {
        if (car == null) return null;
        return mapper.map(car, CarDto.class);
    }

    public Car toEntity(CarDto dto) {
        if (dto == null) return null;
        return mapper.map(dto, Car.class);
    }
}
