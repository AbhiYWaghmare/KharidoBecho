package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.Mobile;
import com.spring.jwt.Mobile.entity.MobileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobileImageRepository extends JpaRepository<MobileImage, Long> {

//    List<Mobile> findAllWithImages();
}