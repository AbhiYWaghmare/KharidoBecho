package com.spring.jwt.laptop.service.impl;

import com.cloudinary.Cloudinary;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.laptop.entity.LaptopPhotos;
import com.spring.jwt.laptop.service.LaptopPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.spring.jwt.laptop.repository.LaptopPhotoRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
                    "laptopId", savedPhoto.getLaptopId(),
                    "type", savedPhoto.getType(),
                    "photo_link", savedPhoto.getPhoto_link(),
                    "publicId", savedPhoto.getPublicId()
            );

        } catch (IOException e) {
            throw new RuntimeException("Image uploading failed: " + e.getMessage());
        }
    }

    @Override
    public Map deleteFile(int photoId) throws IOException {
        LaptopPhotos laptopPhotos = laptopPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id " +photoId));

        try {
            Map result = cloudinary.uploader()
                    .destroy(laptopPhotos.getPublicId(), Collections.emptyMap());

            if("ok".equals(result.get("result"))){
                laptopPhotoRepository.delete(laptopPhotos);
            }
            return result;
        }catch(IOException e){
            throw new RuntimeException("Failed to delete photo " +e.getMessage());
        }
    }

//    public Map deleteFile(String publicId) throws IOException {
//        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//    }


//    @Override
//    public LaptopPhotos savePhoto(LaptopPhotos laptopPhotos) {
//        return laptopPhotoRepository.save(laptopPhotos);
//    }
//
//    @Override
//    public List<LaptopPhotos> getPhotosByLaptopId(int laptopId) {
//        List<LaptopPhotos> photos = laptopPhotoRepository.findByLaptopId(laptopId);
//        if(photos.isEmpty()){
//            throw new ResourceNotFoundException("Photo Not found for laptop id " +laptopId);
//        }
//        return photos;
//    }
//
//    @Override
//    public List<LaptopPhotos> getAllPhotos() {
//        return laptopPhotoRepository.findAll();
//    }
//
//    @Override
//    public void deletePhotoById(int laptopId) {
//        LaptopPhotos existing = laptopPhotoRepository.findById(laptopId)
//                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with laptopId " + laptopId));
//        laptopPhotoRepository.delete(existing);
//    }
}
