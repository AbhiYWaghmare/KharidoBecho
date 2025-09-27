package com.spring.jwt.car.repository;

import com.spring.jwt.car.entity.CloudinaryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CloudinaryImageRepository extends JpaRepository<CloudinaryImage, Long> {
    Optional<CloudinaryImage> findByPublicId(String publicId);
    void deleteByPublicId(String publicId);
    List<CloudinaryImage> findByCarId(Long carId);
}