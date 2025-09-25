package com.spring.jwt.laptop.service.impl;

import com.cloudinary.Cloudinary;
import com.spring.jwt.exception.CloudinaryDeleteException;
import com.spring.jwt.exception.PhotoNotFoundException;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.spring.jwt.laptop.repository.LaptopPhotoRepository;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class LaptopPhotoServiceImpl implements LaptopPhotoService {

    public LaptopPhotoRepository laptopPhotoRepository;
    public Cloudinary cloudinary;

    @Override
    public Map uploadFile(MultipartFile file, int laptopId, String type) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty!");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("resource_type", "auto")
            );

            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            if (secureUrl == null || publicId == null) {
                throw new RuntimeException("Cloudinary returned invalid data");
            }

            LaptopPhotos laptopPhoto = new LaptopPhotos();
            laptopPhoto.setLaptopId(laptopId);
            laptopPhoto.setType(type);
            laptopPhoto.setPhoto_link(secureUrl);
            laptopPhoto.setPublicId(publicId);

            LaptopPhotos savedPhoto = laptopPhotoRepository.save(laptopPhoto);

            return Map.of(
                    "photoId", savedPhoto.getPhotoId(),
//                    "laptopId", savedPhoto.getLaptopId(),
                    "type", savedPhoto.getType(),
                    "photo_link", savedPhoto.getPhoto_link(),
                    "publicId", savedPhoto.getPublicId()
            );

        } catch (IOException e) {
            throw new RuntimeException("Image uploading failed: " + e.getMessage());
        }
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
