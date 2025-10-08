package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.LaptopPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaptopPhotoRepository extends JpaRepository<LaptopPhotos,Long> {
}
