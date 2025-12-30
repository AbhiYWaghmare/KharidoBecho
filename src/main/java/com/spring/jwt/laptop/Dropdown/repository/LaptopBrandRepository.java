package com.spring.jwt.laptop.Dropdown.repository;

import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaptopBrandRepository extends JpaRepository<LaptopBrand,Long> {

    Optional<LaptopBrand> findByBrandNameIgnoreCase(String brandName);

//    List<LaptopBrand> findTop10ByBrandNameStartingWithIgnoreCase(String brandName);
}
