package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.entity.LaptopPhotos;


import java.util.List;

public interface LaptopPhotoService {

    public LaptopPhotos savePhoto(LaptopPhotos laptopPhotos);

    public List<LaptopPhotos> getPhotosByLaptopId(int laptopId);

    public List<LaptopPhotos> getAllPhotos();

    public void deletePhotoById(int laptopId);

}
