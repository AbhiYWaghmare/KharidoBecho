package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.MobileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobileModelRepository extends JpaRepository<MobileModel, Long> {

    boolean existsByNameIgnoreCaseAndBrand_BrandId(String name, Long brandId);

    List<MobileModel> findByBrand_BrandIdAndDeletedFalseOrderByNameAsc(Long brandId);
}

