package com.spring.jwt.repository;

import com.spring.jwt.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByUser_Id(Long userId);
}
