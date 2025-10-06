
package com.spring.jwt.car.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.spring.jwt.car.entity.CloudinaryImage;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.car.repository.CloudinaryImageRepository;
import com.spring.jwt.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryImageRepository imageRepository;
    @Autowired
    private CarRepository carRepository;

//    public Map<String,Object> upload(Mul)


    public Map<String, Object> upload(MultipartFile file) {
        try {
            // Create a Transformation object to specify the size and quality
            Transformation transformation = new Transformation()
                    // Use 'q_auto:low' for a very aggressive compression that prioritizes a small file size
                    .quality("auto:low")
                    // Use 'f_auto' to automatically choose the best format (like WebP) for the requesting browser
                    .fetchFormat("auto")
                    // Optional: resize the image to a smaller size, if needed. For example, limit the width to 800 pixels.
                    // This is often necessary for large source images to stay under a specific file size.
                    .width(800).crop("scale");

            // Build the options map with the aggressive transformation
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "transformation", transformation
            );

            // Upload the image with the specified transformation
            return cloudinary.uploader().upload(file.getBytes(), uploadOptions);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
    @Override
    public Map<String, Object> upload(MultipartFile file, int carId) {
        try {
            Map<String, Object> data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            // ✅ Extract values
            String publicId = data.get("public_id") != null ? data.get("public_id").toString() : null;
            String secureUrl = data.get("secure_url") != null ? data.get("secure_url").toString() : null;

//            find car
            Car car = carRepository.findById(carId).
                    orElseThrow(() -> new RuntimeException("car not found with id " + carId));


            if (publicId != null && secureUrl != null) {
                CloudinaryImage image = new CloudinaryImage();
                image.setPublicId(publicId);
                image.setUrl(secureUrl);
                image.setCar(car);
                imageRepository.save(image);
            }


            return data; // still return cloudinary response
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public Map<String, Object> delete(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // ✅ Also delete from DB
            if ("ok".equals(result.get("result"))) {
                imageRepository.findByPublicId(publicId).ifPresent(imageRepository::delete);
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Image delete failed", e);
        }
    }


    public List<CloudinaryImage> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<CloudinaryImage> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public List<CloudinaryImage> getImagesByCarId(Long carId) {
        return imageRepository.findByCarId(carId);
    }
}
