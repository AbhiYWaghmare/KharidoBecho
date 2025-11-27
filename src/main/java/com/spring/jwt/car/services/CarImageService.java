package com.spring.jwt.car.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarImageService {
    List<String> uploadCarImages(Long carId, List<MultipartFile> files) ;
    void deleteCarImage(Long imageId) ;
}
