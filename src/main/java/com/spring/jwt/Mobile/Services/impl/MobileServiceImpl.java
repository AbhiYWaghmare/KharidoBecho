package com.spring.jwt.Mobile.Services.impl;

import com.spring.jwt.Mobile.Mapper.MobileMapper;
import com.spring.jwt.Mobile.Repository.MobileImageRepository;
import com.spring.jwt.Mobile.Repository.MobileRepository;
import com.spring.jwt.Mobile.Services.MobileService;
import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
import com.spring.jwt.Mobile.entity.Mobile;
import com.spring.jwt.Mobile.entity.MobileImage;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.mobile.MobileImageException;
import com.spring.jwt.exception.mobile.MobileNotFoundException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.utils.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MobileServiceImpl implements MobileService {

    private final MobileRepository mobileRepository;
    private final MobileImageRepository mobileImageRepository;
    private final SellerRepository sellerRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public MobileResponseDTO createMobile(MobileRequestDTO req) {
        Seller seller = sellerRepository.findById(req.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(req.getSellerId()));

        Mobile m = new Mobile();
        MobileMapper.updateFromRequest(m, req);
        m.setSeller(seller);
        m.setDeleted(false);
        m.setStatus(Mobile.Status.ACTIVE);
        m = mobileRepository.save(m);
        return MobileMapper.toDTO(m);
    }

    @Override
    public Page<MobileResponseDTO> listMobiles(int page, int size, Long sellerId) {
        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Mobile> pageResult = (sellerId != null)
                ? mobileRepository.findBySeller_SellerIdAndDeletedFalse(sellerId, p)
                : mobileRepository.findByDeletedFalse(p);
        return pageResult.map(MobileMapper::toDTO);
    }

    @Override
    public MobileResponseDTO getMobile(Long id) {
        Mobile m = mobileRepository.findByMobileIdAndDeletedFalse(id)
                .orElseThrow(() -> new MobileNotFoundException(id));
        return MobileMapper.toDTO(m);
    }

    @Override
    @Transactional
    public MobileResponseDTO updateMobile(Long id, MobileRequestDTO req) {
        Mobile m = mobileRepository.findById(id)
                .orElseThrow(() -> new MobileNotFoundException(id));
        MobileMapper.updateFromRequest(m, req);
        m = mobileRepository.save(m);
        return MobileMapper.toDTO(m);
    }

    @Override
    @Transactional
    public void softDeleteMobile(Long id) {
        Mobile m = mobileRepository.findById(id)
                .orElseThrow(() -> new MobileNotFoundException(id));
        m.setDeleted(true);
        m.setDeletedAt(OffsetDateTime.now());
        m.setStatus(Mobile.Status.DELETED);
        mobileRepository.save(m);
    }

    @Override
    @Transactional
    public List<String> addImages(Long mobileId, List<MultipartFile> files) {
        Mobile mobile = mobileRepository.findById(mobileId)
                .orElseThrow(() -> new MobileNotFoundException(mobileId));

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                // Get full upload result
                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(file, "mobiles");

                String imageUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                // Save both in DB
                MobileImage image = MobileImage.builder()
                        .imageUrl(imageUrl)
                        .publicId(publicId)
                        .mobile(mobile)
                        .build();

                mobileImageRepository.save(image);
                urls.add(imageUrl);

            } catch (IOException e) {
                throw new MobileImageException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }
        return urls;
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        MobileImage image = mobileImageRepository.findById(imageId)
                .orElseThrow(() -> new MobileImageException("Image not found with ID: " + imageId));

        try {
            //  first image will delete from cloudnairy then DB
            boolean deleted = cloudinaryService.deleteByPublicId(image.getPublicId());
            if (!deleted) {
                throw new MobileImageException("Failed to delete image from Cloudinary: " + image.getPublicId());
            }

            //  Then delete from DB
            mobileImageRepository.delete(image);

        } catch (IOException e) {
            throw new MobileImageException("Error deleting image from Cloudinary: " + image.getPublicId(), e);
        }
    }


    public MobileImage uploadMobileImage(Long mobileId, MultipartFile file) {
        Mobile mobile = mobileRepository.findById(mobileId)
                .orElseThrow(() -> new MobileNotFoundException(mobileId));

        try {
            String imageUrl = cloudinaryService.uploadFile(file, "mobiles");
            MobileImage image = new MobileImage();
            image.setMobile(mobile);
            image.setImageUrl(imageUrl);
            return mobileImageRepository.save(image);
        } catch (IOException e) {
            throw new MobileImageException("Failed to upload image: " + file.getOriginalFilename(),e);
        }
    }
}
