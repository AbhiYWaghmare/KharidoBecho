package com.spring.jwt.car.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfigCar {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dc90gxpgd");
        config.put("api_key", "364697521467332");
        config.put("api_secret", "BpmEI1QWe6OUjNDrDvkXzag6YLk");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
