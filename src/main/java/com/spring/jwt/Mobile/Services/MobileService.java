package com.spring.jwt.Mobile.Services;

import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MobileService {
    MobileResponseDTO createMobile(MobileRequestDTO request);
    Page<MobileResponseDTO> listMobiles(int page, int size, Long sellerId);
    MobileResponseDTO getMobile(Long id);
    MobileResponseDTO updateMobile(Long id, MobileRequestDTO request);
    void softDeleteMobile(Long id);
    List<String> addImages(Long mobileId, List<MultipartFile> files) throws Exception;
    void deleteImage(Long imageId);
}
