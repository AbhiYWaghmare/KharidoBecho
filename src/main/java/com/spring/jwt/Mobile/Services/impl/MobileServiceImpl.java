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
import com.spring.jwt.exception.mobile.MobileValidationException;
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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MobileServiceImpl implements MobileService {

    private final MobileRepository mobileRepository;
    private final MobileImageRepository mobileImageRepository;
    private final SellerRepository sellerRepository;
    private final CloudinaryService cloudinaryService;
    private static final long MAX_IMAGE_BYTES = 400 * 1024L; // 400 KB


// This method is for Create mobile service
    private void validateCreateRequest(MobileRequestDTO req) {
        validateCommonFields(req, true); // true = creation mode (all fields required)
    }

    // This method is for Update mobile service
    private void validateUpdateRequest(MobileRequestDTO req) {
        validateCommonFields(req, false); // false = update mode (but still must remain valid)
    }

    //============We create this method because we have to validate this common fileds
    // in both cenarios for Adding Also & for Updating Also so we create common method and implement
    // this in there separte method that we passing in createmobile and updatemobile time method ==========//

    private void validateCommonFields(MobileRequestDTO req, boolean isCreate) {

        // Title
        if (isCreate || req.getTitle() != null) {
            if (req.getTitle() == null || req.getTitle().isBlank()) {
                throw new MobileValidationException("Title is required and cannot be blank.");
            }
        }

        //Description
        if (isCreate || req.getDescription() != null) {
            if (req.getDescription() == null || req.getDescription().isBlank()) {
                throw new MobileValidationException("Description is required.");
            }
            if (req.getDescription().length() > 4000) {
                throw new MobileValidationException("Description cannot exceed 4000 characters.");
            }
        }

        //Price
        if (isCreate || req.getPrice() != null) {
            if (req.getPrice() == null) {
                throw new MobileValidationException("Price is required.");
            }
            if (req.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new MobileValidationException("Price must be greater than zero.");
            }
            if (req.getPrice().compareTo(new BigDecimal("10000000")) > 0) {
                throw new MobileValidationException("Price cannot exceed 1 crore.");
            }
        }

        //Condition
        if (isCreate || req.getCondition() != null) {
            if (req.getCondition() == null || req.getCondition().isBlank()) {
                throw new MobileValidationException("Condition is required.");
            }

            try {
                Mobile.Condition.valueOf(req.getCondition().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new MobileValidationException("Invalid condition. Allowed: NEW, USED, REFURBISHED.");
            }
        }

        //Year of Purchase
        if (isCreate || req.getYearOfPurchase() != null) {
            if (req.getYearOfPurchase() == null) {
                throw new MobileValidationException("Year of purchase is required.");
            }
            int currentYear = Year.now().getValue();
            if (req.getYearOfPurchase() > currentYear) {
                throw new MobileValidationException("Year of purchase cannot be in the future.");
            }
            if (req.getYearOfPurchase() < 2000) {
                throw new MobileValidationException("Year of purchase must be after 2000.");
            }
        }

        //Brand
        if (isCreate || req.getBrand() != null) {
            if (req.getBrand() == null || req.getBrand().isBlank()) {
                throw new MobileValidationException("Brand is required.");
            }
        }

        // Model
        if (isCreate || req.getModel() != null) {
            if (req.getModel() == null || req.getModel().isBlank()) {
                throw new MobileValidationException("Model is required.");
            }
        }

        //  Color
        if (isCreate || req.getColor() != null) {
            if (req.getColor() == null || req.getColor().isBlank()) {
                throw new MobileValidationException("Color is required.");
            }
        }

        //Seller
        if (isCreate || req.getSellerId() != null) {
            if (req.getSellerId() == null) {
                throw new MobileValidationException("SellerId is required.");
            }
        }
    }



    @Override
    @Transactional
    public MobileResponseDTO createMobile(MobileRequestDTO req) {

        validateCreateRequest(req);

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
        Page<Mobile> pageResult = (sellerId != null) ? mobileRepository.findBySeller_SellerIdAndDeletedFalse(sellerId, p)
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

        validateUpdateRequest(req);

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
            // Basic validation
            if (file == null || file.isEmpty()) {
                throw new MobileImageException("One of the uploaded files is empty");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new MobileImageException("Invalid file type: " + contentType);
            }

            try {
                MultipartFile toUpload = file;

                // If file is bigger than max allowed, try to compress
                if (file.getSize() > MAX_IMAGE_BYTES) {
                    byte[] compressed = compressImageToMaxSize(file, MAX_IMAGE_BYTES);
                    if (compressed == null || compressed.length == 0) {
                        throw new MobileImageException("Failed to compress image: " + file.getOriginalFilename());
                    }
                    if (compressed.length > MAX_IMAGE_BYTES) {
                        throw new MobileImageException("Cannot reduce image below 400KB: " + file.getOriginalFilename());
                    }
                    // wrap bytes into MultipartFile so it can be passed to CloudinaryService
                    toUpload = new ByteArrayMultipartFile(
                            compressed,
                            file.getOriginalFilename(),
                            file.getContentType()
                    );
                }

                // Upload to Cloudinary and get full result (secure_url + public_id)
                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(toUpload, "mobiles");
                String imageUrl = Objects.toString(uploadResult.get("secure_url"), null);
                String publicId = Objects.toString(uploadResult.get("public_id"), null);

                //  MobileImage with both URL and publicId
                MobileImage img = MobileImage.builder()
                        .imageUrl(imageUrl)
                        .publicId(publicId)
                        .mobile(mobile)
                        .build();
                mobileImageRepository.save(img);

                urls.add(imageUrl);

            } catch (IOException e) {
                throw new MobileImageException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        return urls;
    }


    //  compress image to fit under maxBytes (tries lowering JPEG quality)(Helper)
    private byte[] compressImageToMaxSize(MultipartFile file, long maxBytes) throws IOException {

        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) return null;

        // We provide output in JPEG. This may strip transparency if source was PNG.
        String formatName = "jpg";

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        float quality = 0.90f; // start quality
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Iteratively try reducing quality and then size until under maxBytes
        for (int attempt = 0; attempt < 10; attempt++) {
            baos.reset();

            // create a scaled copy if we shrank dimensions
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(inputImage, 0, 0, width, height, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
            if (!writers.hasNext()) {
                break;
            }
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0.10f, quality)); // limit lower bound
            }

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(outputImage, null, null), param);
                writer.dispose();
            }

            // If under max, return bytes
            if (baos.size() <= maxBytes) {
                return baos.toByteArray();
            }

            // otherwise reduce quality first, then dimensions
            if (quality > 0.35f) {
                quality -= 0.15f; // reduce compression quality
            } else {
                // reduce dimensions by 90% to try further reduction
                width = Math.max(100, (int) (width * 0.9));
                height = Math.max(100, (int) (height * 0.9));
            }
        }

        // final attempt: return whatever we have (may exceed maxBytes) or null
        return baos.size() > 0 ? baos.toByteArray() : null;
    }

    // Small adapter class to wrap compressed bytes into a MultipartFile
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] bytes;
        private final String originalFilename;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] bytes, String originalFilename, String contentType) {
            this.bytes = bytes;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return originalFilename;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes == null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes == null ? 0 : bytes.length;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(bytes);
            }
        }
    }

    // ================================================= //
    // in this method the image size is not defined //
  //==========================================================//

//    @Override
//    @Transactional
//    public List<String> addImages(Long mobileId, List<MultipartFile> files) {
//        Mobile mobile = mobileRepository.findById(mobileId)
//                .orElseThrow(() -> new MobileNotFoundException(mobileId));
//
//        List<String> urls = new ArrayList<>();
//        for (MultipartFile file : files) {
//            try {
//                // Get  upload result
//                Map<String, Object> uploadResult = cloudinaryService.uploadFileWithResult(file, "mobiles");
//
//                String imageUrl = (String) uploadResult.get("secure_url");
//                String publicId = (String) uploadResult.get("public_id");
//
//                // Save both in DB
//                MobileImage image = MobileImage.builder()
//                        .imageUrl(imageUrl)
//                        .publicId(publicId)
//                        .mobile(mobile)
//                        .build();
//
//                mobileImageRepository.save(image);
//                urls.add(imageUrl);
//
//            } catch (IOException e) {
//                throw new MobileImageException("Failed to upload image: " + file.getOriginalFilename(), e);
//            }
//        }
//        return urls;
//    }


    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        MobileImage image = mobileImageRepository.findById(imageId)
                .orElseThrow(() -> new MobileImageException("Mobile image not found with ID: " + imageId));

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
