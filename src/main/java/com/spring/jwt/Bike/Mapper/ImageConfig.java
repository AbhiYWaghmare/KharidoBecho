package com.spring.jwt.Bike.Mapper;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class ImageConfig {
    @Bean
    public Cloudinary getCloudinary(){

      Map config= new HashMap();
      config.put("cloud_name","djfj8mwri");
      config.put("api_key","511134529942189");
      config.put("api_secret","4kCCbGN0OLC4JbPy-vyON_ROmR0");
      config.put("secure",true);

       return new Cloudinary(config);
    }
}
