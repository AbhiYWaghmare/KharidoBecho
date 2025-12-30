package com.spring.jwt.laptop.Dropdown.repository;

import com.spring.jwt.laptop.Dropdown.entity.LaptopBrand;
import com.spring.jwt.laptop.Dropdown.entity.LaptopModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaptopModelRepository extends JpaRepository<LaptopModel,Long> {

    Optional<LaptopModel> findByBrandAndModelNameIgnoreCase(
            LaptopBrand brand,
            String modelName
    );

//    List<LaptopBrand> findTop10ByBrandNameStartingWithIgnoreCase(String brandName);
}
