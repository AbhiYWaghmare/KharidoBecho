package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.MobileBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobileBrandRepository extends JpaRepository<MobileBrand, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<MobileBrand> findByDeletedFalseOrderByNameAsc();

    boolean existsByBrandIdAndDeletedFalse(Long brandId);
}

