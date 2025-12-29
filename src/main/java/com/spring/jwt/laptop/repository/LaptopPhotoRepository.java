package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LaptopPhotoRepository extends JpaRepository<LaptopPhotos,Long> {
}
