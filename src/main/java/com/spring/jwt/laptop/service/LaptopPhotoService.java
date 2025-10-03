package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.entity.LaptopPhotos;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface LaptopPhotoService {

    LaptopPhotos uploadSingleFile(MultipartFile file, Long laptopId, String type) throws IOException;

    List<LaptopPhotos> uploadFile(MultipartFile[] files, Long laptopId, String type) throws IOException;

    Map deleteFile(int laptopId, int photoId);
}
