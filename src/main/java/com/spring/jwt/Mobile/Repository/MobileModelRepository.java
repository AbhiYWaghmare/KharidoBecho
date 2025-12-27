package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.Brand;
import com.spring.jwt.Mobile.entity.MobileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MobileModelRepository extends JpaRepository<MobileModel, Long> {

    Optional<MobileModel> findByNameIgnoreCaseAndBrand(
            String name, Brand brand
    );

    List<MobileModel> findTop10ByBrandAndNameStartingWithIgnoreCase(
            Brand brand, String name
    );
}
