package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.MobileRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobileRequestRepository extends JpaRepository<MobileRequest, Long> {
    List<MobileRequest> findByMobile_MobileIdOrderByCreatedAtAsc(Long mobileId);
    List<MobileRequest> findByBuyer_BuyerId(Long buyerId);
    boolean existsByMobile_MobileIdAndStatus(Long mobileId, RequestStatus status);
    List<MobileRequest> findByMobile_MobileIdAndStatus(Long mobileId, RequestStatus status);
}
