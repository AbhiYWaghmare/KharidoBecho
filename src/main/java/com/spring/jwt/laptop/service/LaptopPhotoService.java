package com.spring.jwt.laptop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface LaptopPhotoService {

    List<String> uploadPhoto(Long laptopId, List<MultipartFile> files) ;
   void deleteImage(Long imageId);


}
