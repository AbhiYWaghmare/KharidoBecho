package com.spring.jwt.car.repository;

import com.spring.jwt.car.entity.CarPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarPhotoRepository extends JpaRepository<CarPhotos, Long> {
    List<CarPhotos> findByCar_CarId(Long carId);
}
