package com.spring.jwt.laptop.repository;

import com.spring.jwt.laptop.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
     boolean existsBySerialNumber(String serialNumber);
}
