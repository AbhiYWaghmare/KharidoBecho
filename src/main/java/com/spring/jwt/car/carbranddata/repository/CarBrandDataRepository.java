package com.spring.jwt.car.carbranddata.repository;

import com.spring.jwt.car.carbranddata.entity.BrandData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandDataRepository extends JpaRepository<BrandData, Integer> {

    Optional<BrandData> findByBrandAndVariantAndSubVariant(
            String brand, String variant, String subVariant);

    List<BrandData> findByBrand(String brand);

    List<BrandData> findByBrandAndVariant(String brand, String variant);

    @Query("SELECT DISTINCT b.brand FROM BrandData b")
    List<String> findDistinctBrands();
}
