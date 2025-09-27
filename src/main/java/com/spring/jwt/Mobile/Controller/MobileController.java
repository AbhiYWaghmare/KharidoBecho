package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.Services.MobileService;
import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

//********************************************************//

    //Author : Abhishek Waghmare
    //Mobile Controller
    //Date : 22/09/2025

//*******************************************************//

@RestController
@RequestMapping("/api/v1/mobiles")
@RequiredArgsConstructor
public class MobileController {

    private final MobileService mobileService;

    //To Add mobile for sell
    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO> createMobile(@RequestBody MobileRequestDTO request) {
        mobileService.createMobile(request);

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code("200")
                .message("Mobile Added Successfully !!")
                .build();

        return ResponseEntity.ok(response);
    }


    //To get all mobiles
    @GetMapping("/getAllMobiles")
    public ResponseEntity<Page<MobileResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long sellerId
    ) {
        Page<MobileResponseDTO> p = mobileService.listMobiles(page, size, sellerId);
        return ResponseEntity.ok(p);
    }

    //To get mobile by ID
    @GetMapping("/{id}")
    public ResponseEntity<MobileResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(mobileService.getMobile(id));
    }

    //To update the details of mobile by id
    @PatchMapping("/update/{id}")
    public ResponseEntity<MobileResponseDTO> update(@PathVariable Long id, @RequestBody MobileRequestDTO req) {
        return ResponseEntity.ok(mobileService.updateMobile(id, req));
    }

    //To delete mobile by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id) {
        mobileService.softDeleteMobile(id);
        return ResponseEntity.ok(Map.of("status","success","message","Mobile soft-deleted"));
    }

    //To upload images of particular mobile by ID
    @PostMapping("/{id}/upload/images")
    public ResponseEntity<List<String>> uploadImages(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files)  {
        List<String> urls = mobileService.addImages(id, files);
        return ResponseEntity.ok(urls);
    }



        //To delete images By Image ID
        @DeleteMapping("/images/delete/{imageId}")
        public ResponseEntity<BaseResponseDTO> deleteImage(@PathVariable Long imageId) {
            mobileService.deleteImage(imageId);

            BaseResponseDTO response = BaseResponseDTO.builder()
                    .code(String.valueOf(HttpStatus.OK.value()))
                    .message("Image deleted successfully")
                    .build();

            return ResponseEntity.ok(response);
        }
    }


