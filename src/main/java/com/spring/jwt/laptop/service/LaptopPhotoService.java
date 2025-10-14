package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.entity.LaptopPhotos;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface LaptopPhotoService {

    List<String> uploadPhoto(Long photoId, List<MultipartFile> files) ;
   void deleteImage(Long imageId);


}
