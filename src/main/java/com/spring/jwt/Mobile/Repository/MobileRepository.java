package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.Mobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileRepository extends JpaRepository<Mobile, Long> {
    Page<Mobile> findByDeletedFalse(Pageable pageable);
    Page<Mobile> findBySeller_SellerIdAndDeletedFalse(Long sellerId, Pageable pageable);
    java.util.Optional<Mobile> findByMobileIdAndDeletedFalse(Long id);
}
