package com.spring.jwt.laptop.service.impl;

import com.cloudinary.Cloudinary;
import com.spring.jwt.exception.CloudinaryDeleteException;
import com.spring.jwt.exception.PhotoNotFoundException;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.exceptions.LaptopNotFoundException;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.spring.jwt.laptop.repository.LaptopPhotoRepository;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class LaptopPhotoServiceImpl implements LaptopPhotoService {

    public LaptopPhotoRepository laptopPhotoRepository;
    public Cloudinary cloudinary;
    public LaptopRepository laptopRepository;

    @Override
    @Transactional
    public LaptopPhotos uploadSingleFile(MultipartFile file, Long laptopId, String type) throws IOException {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new LaptopNotFoundException("Laptop not found"));

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "image", "folder", "laptop_photos"));

        LaptopPhotos photo = new LaptopPhotos();
        photo.setPhoto_link(uploadResult.get("secure_url").toString());
        photo.setPublicId(uploadResult.get("public_id").toString());
        photo.setType(type);
        photo.setLaptop(laptop);

        laptop.getLaptopPhotos().add(photo);
        laptopRepository.save(laptop);

        return photo;
    }

    @Override
    @Transactional
    public List<LaptopPhotos> uploadFile(MultipartFile[] files, Long laptopId, String type) throws IOException {
        List<LaptopPhotos> photos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                LaptopPhotos photo = uploadSingleFile(file, laptopId, type);
                photos.add(photo);
            }
        }

        return photos;
    }



    @Override
    public Map deleteFile(int laptopId, int photoId) {

        LaptopPhotos laptopPhotos = laptopPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

        try {
           Map result =  cloudinary.uploader().destroy(laptopPhotos.getPublicId(), ObjectUtils.emptyMap());
            if (!"ok".equals(result.get("result"))) {
                throw new CloudinaryDeleteException("Failed to delete from Cloudinary: " + result);
            }

            laptopPhotoRepository.delete(laptopPhotos);

            Map<String, String> resp = new HashMap<>();
            resp.put("message", "Photo deleted successfully");
            return resp;

        }catch (IOException e) {
           throw new RuntimeException("Error deleting photo: " + e.getMessage());
        }

    }


}
