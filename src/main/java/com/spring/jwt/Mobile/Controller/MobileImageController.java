package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.Services.MobileService;
import com.spring.jwt.Mobile.dto.ImageUploadResponseDTO;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mobile-images")
@RequiredArgsConstructor
public class MobileImageController {

    private final MobileService mobileService;

    //********************************************************//

    //Author : Abhishek Waghmare
    //Mobile Controller
    //Date : 29/09/2025

//*******************************************************//


    //To upload images of particular mobile by ID
    @PostMapping("/{id}/upload")
    public ResponseEntity<ImageUploadResponseDTO> uploadImages(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {
        List<String> urls = mobileService.addImages(id, files);

        ImageUploadResponseDTO response = ImageUploadResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Image uploaded sucessfully")
                .images(urls)
                .build();

        return ResponseEntity.ok(response);
    }


    //To delete images By Image ID
    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<BaseResponseDTO> deleteImage(@PathVariable Long imageId) {
        mobileService.deleteImage(imageId);

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("Image deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}