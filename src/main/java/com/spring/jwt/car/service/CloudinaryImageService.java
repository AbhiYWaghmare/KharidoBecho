package com.spring.jwt.car.service;

import com.spring.jwt.car.entity.CloudinaryImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CloudinaryImageService {
    Map<String, Object> upload(MultipartFile file, int carId);
    Map<String, Object> delete(String publicId);

    List<CloudinaryImage> getAllImages();
    Optional<CloudinaryImage> getImageById(Long id);
    List<CloudinaryImage> getImagesByCarId(Long carId);
}
