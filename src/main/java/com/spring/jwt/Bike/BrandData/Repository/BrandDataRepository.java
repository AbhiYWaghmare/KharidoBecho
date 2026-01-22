package com.spring.jwt.Bike.BrandData.Repository;

import com.spring.jwt.Bike.BrandData.Entity.BikeBrandData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandDataRepository extends JpaRepository<BikeBrandData, Integer> {
    Optional<BikeBrandData> findByBrandAndModelAndVariant(
            String brand,
            String model,
            String variant
    );
    List<BikeBrandData> findByBrand(String brand);
    List<BikeBrandData> findByBrandAndModel(String brand, String model);
    @Query("SELECT DISTINCT b.brand FROM BikeBrandData b")
    List<String> findDistinctBrands();
    @Query("""
       SELECT b.engineCC
       FROM BikeBrandData b
       WHERE b.brand = :brand
         AND b.model = :model
         AND b.variant = :variant
       """)
    Integer findEngineCcByBrandModelVariant(
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("variant") String variant
    );



}
