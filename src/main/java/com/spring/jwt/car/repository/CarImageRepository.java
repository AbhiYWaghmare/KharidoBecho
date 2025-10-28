package com.spring.jwt.car.repository;

import com.spring.jwt.car.entity.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
}
