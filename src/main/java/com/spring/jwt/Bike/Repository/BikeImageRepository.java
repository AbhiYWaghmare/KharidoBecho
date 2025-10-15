package com.spring.jwt.Bike.Repository;

import com.spring.jwt.Bike.Entity.Bike;
import org.springframework.stereotype.Repository;


import com.spring.jwt.Bike.Entity.BikeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Repository
public interface BikeImageRepository extends JpaRepository<BikeImage, Integer> {

    // Fetch all images for a specific bike
    List<BikeImage> findByBike(Bike bike);




}


