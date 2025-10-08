package com.spring.jwt.car.service;

import com.spring.jwt.car.entity.CarPhotos;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarPhotoService {

    //  Upload multiple car photos (returns URLs of uploaded images)
    List<String> uploadPhoto(Long carId, List<MultipartFile> files) throws IOException;

    //  Upload a single car image (optional helper)
    CarPhotos uploadCarImage(Long carId, MultipartFile file);

    //  Delete image by ID
    void deleteImage(Long photoId);
}
