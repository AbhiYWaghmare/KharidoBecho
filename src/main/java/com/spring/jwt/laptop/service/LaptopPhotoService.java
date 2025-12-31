package com.spring.jwt.laptop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface LaptopPhotoService {

    List<String> uploadPhoto(Long photoId, List<MultipartFile> files) ;


   void deleteImage(Long imageId);


}
