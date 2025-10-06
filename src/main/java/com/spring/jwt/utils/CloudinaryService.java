package com.spring.jwt.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    //===================================================================================================================================================//
    //Uploads file to Cloudinary and returns only the secure URL & compress the image get in bytes then upload(for backward compatibility).

//    public String uploadFile(byte[] data, String folder) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap("folder", folder));
//        return uploadResult.get("secure_url").toString();
//    }

    //======================================================================================================================================================//

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", folder));
        return uploadResult.get("secure_url").toString();
    }

    //Uploads file and returns the full response Map so we can store public_id & URL.

    public Map<String, Object> uploadFileWithResult(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", folder));
    }

    //Deletes file from Cloudinary using its public_id.
    // Returns true if successfully deleted or already not found.

    public boolean deleteByPublicId(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
        Object res = result.get("result");
        if (res == null) return false;
        String r = String.valueOf(res);
        return "ok".equalsIgnoreCase(r) || "not found".equalsIgnoreCase(r) || "not_found".equalsIgnoreCase(r);
    }
}



































































//package com.spring.jwt.utils;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class CloudinaryService {
//
//    private final Cloudinary cloudinary;
//
//    public CloudinaryService(
//            @Value("${cloudinary.cloud-name}") String cloudName,
//            @Value("${cloudinary.api-key}") String apiKey,
//            @Value("${cloudinary.api-secret}") String apiSecret) {
//        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", cloudName,
//                "api_key", apiKey,
//                "api_secret", apiSecret));
//    }
//
//    public String uploadFile(MultipartFile file, String folder) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                ObjectUtils.asMap("folder", folder));
//        return uploadResult.get("secure_url").toString();
//    }
//}
