package com.spring.jwt.repository;

import com.spring.jwt.entity.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {


     Buyer findByUser_Id(Long userId);

     // Fetch only buyers that are not soft-deleted, paginated
     Page<Buyer> findByDeletedFalse(Pageable pageable);
}
