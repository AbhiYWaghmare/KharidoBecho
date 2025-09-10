package com.spring.jwt.repository;

import com.spring.jwt.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

     // Spring Data JPA will automatically implement this method
     Buyer findByUserId(Integer userId);  //  (notice user.id)

}
