package com.spring.jwt.laptop.Dropdown.repository;

import com.spring.jwt.laptop.Dropdown.entity.LaptopBrandModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LaptopBrandModelRepository extends JpaRepository<LaptopBrandModel,Long> {
    Optional<LaptopBrandModel> findByBrandAndModel(String brand, String model);

    List<LaptopBrandModel> findByBrand(String brand);
    @Query("SELECT DISTINCT lbm.brand FROM LaptopBrandModel lbm")
    List<String> findDistinctBrands();

}
