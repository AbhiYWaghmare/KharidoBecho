package com.spring.jwt.car.carbranddata.repository;

import com.spring.jwt.car.carbranddata.entity.CarBrandData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarBrandDataRepository extends JpaRepository<CarBrandData, Integer> {

    Optional<CarBrandData> findByBrandAndVariantAndSubVariant(
            String brand, String variant, String subVariant);

    List<CarBrandData> findByBrand(String brand);

    List<CarBrandData> findByBrandAndVariant(String brand, String variant);

    // Fixed query to use correct entity
    @Query("SELECT DISTINCT c.brand FROM CarBrandData c")
    List<String> findDistinctBrands();
}
