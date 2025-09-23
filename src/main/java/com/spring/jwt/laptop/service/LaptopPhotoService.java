package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.entity.LaptopPhotos;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface LaptopPhotoService {
//    LaptopPhotos savePhoto(LaptopPhotos laptopPhotos);
//
//    List<LaptopPhotos> getPhotosByLaptopId(int laptopId);
//
//    List<LaptopPhotos> getAllPhotos();
//
//    void deletePhotoById(int laptopId);

    Map uploadFile(MultipartFile file, int laptopId, String type);


    Map deleteFile(int photoId) throws IOException;
}
