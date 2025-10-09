package com.spring.jwt.Bike.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip mapping of ID field to avoid identifier change issues
        modelMapper.typeMap(com.spring.jwt.Bike.dto.bikeDto.class, com.spring.jwt.Bike.Entity.Bike.class)
                .addMappings(mapper -> mapper.skip(com.spring.jwt.Bike.Entity.Bike::setBike_id));

        // Return the configured instance
        return modelMapper;
    }
}

