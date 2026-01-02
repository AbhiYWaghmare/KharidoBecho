package com.spring.jwt.Mobile.Services.impl;

import com.spring.jwt.Mobile.Mapper.MobileMapper;
//import com.spring.jwt.Mobile.Repository.BrandRepository;
import com.spring.jwt.Mobile.Repository.MobileImageRepository;
//import com.spring.jwt.Mobile.Repository.MobileModelRepository;
import com.spring.jwt.Mobile.Repository.MobileModelRepository;
import com.spring.jwt.Mobile.Repository.MobileRepository;
import com.spring.jwt.Mobile.Services.MobileService;
import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
import com.spring.jwt.Mobile.dto.MobileUpdateDTO;
//import com.spring.jwt.Mobile.entity.Brand;
import com.spring.jwt.Mobile.entity.Mobile;
import com.spring.jwt.Mobile.entity.MobileImage;
//import com.spring.jwt.Mobile.entity.MobileModel;
import com.spring.jwt.Mobile.entity.MobileModel;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.mobile.MobileImageException;
import com.spring.jwt.exception.mobile.MobileNotFoundException;
import com.spring.jwt.exception.mobile.MobileValidationException;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.utils.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.spring.jwt.utils.ByteArrayMultipartFile;
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
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileServiceImpl implements MobileService {

    private final MobileRepository mobileRepository;
//    private final BrandRepository brandRepository;
    private final MobileModelRepository mobileModelRepository;
    private final MobileImageRepository mobileImageRepository;
    private final SellerRepository sellerRepository;
    private final CloudinaryService cloudinaryService;
    private static final long MAX_IMAGE_BYTES = 400 * 1024L; // 400 KB


    // This method is for Create mobile service
    private void validateCreateRequest(MobileRequestDTO req) {
        validateCommonCreateFields(req); //  all fields required
    }

    // This method is for Update mobile service
    private void validateUpdateRequest(MobileUpdateDTO req) {

        if (req.getTitle() == null &&
                req.getDescription() == null &&
                req.getPrice() == null &&
                req.getNegotiable() == null &&
                req.getCondition() == null &&
//                req.getBrand() == null &&
//                req.getModel() == null &&
                req.getColor() == null &&
                req.getYearOfPurchase() == null) {

            throw new MobileValidationException("Update request body cannot be empty.");
        }

        validateCommonUpdateFields(req);
    }

    //When we want suggestion from database in sorting order(Abhi W)
//    private MobileModel resolveModel(String brandName, String modelName) {
//
//        String cleanBrand = brandName.trim().toUpperCase();
//        String cleanModel = modelName.trim().toUpperCase();
//
//        Brand brand = brandRepository.findByNameIgnoreCase(cleanBrand)
//                .orElseGet(() ->
//                        brandRepository.save(
//                                Brand.builder().name(cleanBrand).build()
//                        )
//                );
//
//        return mobileModelRepository
//                .findByNameIgnoreCaseAndBrand(cleanModel, brand)
//                .orElseGet(() ->
//                        mobileModelRepository.save(
//                                MobileModel.builder()
//                                        .name(cleanModel)
//                                        .brand(brand)
//                                        .build()
//                        )
//                );
//    }



    //============We create this method because we have to validate this common fileds
    // in both cenarios for Adding Also & for Updating Also so we create common method and implement
    // this in there separte method that we passing in createmobile and updatemobile time method ==========//

//    private void validateCommonFields(MobileRequestDTO req, boolean isCreate) {
//
//        //  Seller Check
//        if (isCreate || req.getSellerId() != null) {
//            Seller seller = sellerRepository.findById(req.getSellerId())
//                    .orElseThrow(() -> new SellerNotFoundException(req.getSellerId()));
//
//            if (Boolean.TRUE.equals(seller.isDeleted())) {
//                throw new MobileValidationException("Seller is deleted or inactive.");
//            }
//        }
//
//        //  Year Check
//        if (req.getYearOfPurchase() != null) {
//            int currentYear = Year.now().getValue();
//            if (req.getYearOfPurchase() > currentYear) {
//                throw new MobileValidationException("Year of purchase cannot be in the future.");
//            }
//            if (req.getYearOfPurchase() < 2000) {
//                throw new MobileValidationException("Year of purchase must be after 2000.");
//            }
//        }
//
//        //  Price Check
//        if (req.getPrice() != null) {
//            BigDecimal price = req.getPrice();
//            if (price.compareTo(BigDecimal.ZERO) <= 0) {
//                throw new MobileValidationException("Price must be greater than zero.");
//            }
//            if (price.compareTo(BigDecimal.valueOf(10_000_000)) > 0) {
//                throw new MobileValidationException("Price cannot exceed 1 crore.");
//            }
//            req.setPrice(price.setScale(2, RoundingMode.HALF_UP));
//        }
//
//        // Condition Check
//        if (req.getCondition() != null) {
//            try {
//                Mobile.Condition.valueOf(req.getCondition().toUpperCase());
//            } catch (IllegalArgumentException e) {
//                throw new MobileValidationException("Invalid condition value. Use NEW, USED, or REFURBISHED.");
//            }
//        }
//
//        // Duplicate Check
//        if (isCreate && mobileRepository.existsByTitleAndSeller_SellerId(req.getTitle(), req.getSellerId())) {
//            throw new MobileValidationException("Duplicate listing: same title already exists for this seller.");
//        }
//
//        //  Title Check
//        if (req.getTitle() != null && req.getTitle().length() > 150) {
//            throw new MobileValidationException("Title too long. Max 150 characters allowed.");
//        }
//
//        // Description Check
//        if (req.getDescription() != null) {
//            int words = req.getDescription().trim().split("\\s+").length;
//            if (words < 5) throw new MobileValidationException("Description must have at least 5 words.");
//            if (words > 70) throw new MobileValidationException("Description cannot exceed 70 words.");
//        }
//    }

    private void validateCommonCreateFields(MobileRequestDTO req) {

        // Seller check (CREATE ONLY)
        Seller seller = sellerRepository.findById(req.getSellerId())
                .orElseThrow(() -> new SellerNotFoundException(req.getSellerId()));

        if (Boolean.TRUE.equals(seller.isDeleted())) {
            throw new MobileValidationException("Seller is deleted or inactive.");
        }

        // Duplicate check (CREATE ONLY)
        if (mobileRepository.existsByTitleAndSeller_SellerId(
                req.getTitle(), req.getSellerId())) {
            throw new MobileValidationException(
                    "Duplicate listing: same title already exists for this seller."
            );
        }

        validateSharedFields(
                req.getTitle(),
                req.getDescription(),
                req.getPrice(),
                req.getCondition(),
                req.getYearOfPurchase(),
                req.getState(),
                req.getCity(),
                req.getAddress()
        );
    }

    private void validateCommonUpdateFields(MobileUpdateDTO req) {

        validateSharedFields(
                req.getTitle(),
                req.getDescription(),
                req.getPrice(),
                req.getCondition(),
                req.getYearOfPurchase(),
                req.getState(),
                req.getCity(),
                req.getAddress()
        );
    }

    private void validateSharedFields(
            String title,
            String description,
            BigDecimal price,
            String condition,
            Integer yearOfPurchase,
            String state,
            String city,
            String address

    ) {

        // Year check
        if (yearOfPurchase != null) {
            int currentYear = Year.now().getValue();
            if (yearOfPurchase > currentYear) {
                throw new MobileValidationException("Year of purchase cannot be in the future.");
            }
            if (yearOfPurchase < 2000) {
                throw new MobileValidationException("Year of purchase must be after 2000.");
            }
        }

        // Price check
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new MobileValidationException("Price must be greater than zero.");
            }
            if (price.compareTo(BigDecimal.valueOf(10_000_000)) > 0) {
                throw new MobileValidationException("Price cannot exceed 1 crore.");
            }
        }

        // Condition check
        if (condition != null) {
            try {
                Mobile.Condition.valueOf(condition.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new MobileValidationException(
                        "Invalid condition value. Use NEW, USED, or REFURBISHED."
                );
            }
        }

        // Title check
        if (title != null && title.length() > 150) {
            throw new MobileValidationException("Title too long. Max 150 characters allowed.");
        }

        if (state != null && state.length() > 100) {
            throw new MobileValidationException("State name too long.");
        }

        if (city != null && city.length() > 100) {
            throw new MobileValidationException("City name too long.");
        }

        if (address != null && address.length() > 255) {
            throw new MobileValidationException("Address too long.");
        }


        // Description check
        if (description != null) {
            int words = description.trim().split("\\s+").length;
            if (words < 5) {
                throw new MobileValidationException("Description must have at least 5 words.");
            }
            if (words > 70) {
                throw new MobileValidationException("Description cannot exceed 70 words.");
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
//        MobileModel model = resolveModel(req.getBrand(), req.getModel());
        MobileMapper.updateFromRequest(m, req);
//        m.setModel(model);
        MobileModel model = mobileModelRepository.findById(req.getModelId())
                .orElseThrow(() -> new MobileValidationException("Invalid model selected"));

        m.setModel(model);

        m.setSeller(seller);
        m.setDeleted(false);
        m.setStatus(Mobile.Status.ACTIVE);
        m = mobileRepository.save(m);
        return MobileMapper.toDTO(m);

    }




//    @Override
//    public Page<MobileResponseDTO> listMobiles(int page, int size, Long sellerId) {
//        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        Page<Mobile> pageResult = (sellerId != null) ? mobileRepository.findBySeller_SellerIdAndDeletedFalse(sellerId, p)
//                : mobileRepository.findByDeletedFalse(p);
//        return pageResult.map(MobileMapper::toDTO);
//    }

    @Override
    @Transactional
    public Page<MobileResponseDTO> listMobiles(int page, int size, Long sellerId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Mobile> pageResult = (sellerId != null)
                ? mobileRepository.findBySeller_SellerIdAndDeletedFalse(sellerId, pageable)
                : mobileRepository.findByDeletedFalse(pageable);

        // Collect IDs
        List<Long> ids = pageResult.getContent()
                .stream()
                .map(Mobile::getMobileId)
                .toList();

        // Fetch mobiles WITH images
        List<Mobile> mobilesWithImages = ids.isEmpty()
                ? List.of()
                : mobileRepository.findWithImagesByIds(ids);

        // Map by ID
        Map<Long, Mobile> mobileMap = mobilesWithImages.stream()
                .collect(Collectors.toMap(Mobile::getMobileId, m -> m));

        // Map to DTOs with SAFETY CHECK
        List<MobileResponseDTO> dtoList = pageResult.getContent().stream()
                .map(m -> {
                    Mobile fullMobile = mobileMap.get(m.getMobileId());
                    if (fullMobile == null) {
                        throw new MobileNotFoundException(
                                "Mobile not found while mapping images. ID=" + m.getMobileId()
                        );
                    }
                    return MobileMapper.toDTO(fullMobile);
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, pageResult.getTotalElements());
    }



//    @Override
//    public MobileResponseDTO getMobile(Long id) {
//        Mobile m = mobileRepository.findByMobileIdAndDeletedFalse(id)
//                .orElseThrow(() -> new MobileNotFoundException(id));
//        return MobileMapper.toDTO(m);
//    }

    @Override
    @Transactional
    public MobileResponseDTO getMobile(Long id) {
        Mobile m = mobileRepository.findOneWithImages(id)
                .orElseThrow(() -> new MobileNotFoundException(id));
        return MobileMapper.toDTO(m);
    }




    @Override
    @Transactional
    public MobileResponseDTO updateMobile(Long id, MobileUpdateDTO req) {

        validateUpdateRequest(req);

        Mobile m = mobileRepository.findById(id)
                .orElseThrow(() -> new MobileNotFoundException(id));

        if (m.isDeleted()) {
            throw new MobileValidationException("Cannot update a deleted mobile.");
        }

        MobileMapper.updateFromRequest(m, req);
//        if (req.getBrand() != null && req.getModel() != null) {
//            MobileModel model = resolveModel(req.getBrand(), req.getModel());
//            m.setModel(model);
//        }
        if (req.getModelId() != null) {
            MobileModel model = mobileModelRepository.findById(req.getModelId())
                    .orElseThrow(() -> new MobileValidationException("Invalid model selected"));
            m.setModel(model);
        }

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


    //  compress image to fit under maxBytes (tries lowering JPEG quality)
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

//    // Small adapter class to wrap compressed bytes into a MultipartFile
//    private static class ByteArrayMultipartFile implements MultipartFile {
//        private final byte[] bytes;
//        private final String originalFilename;
//        private final String contentType;
//
//        public ByteArrayMultipartFile(byte[] bytes, String originalFilename, String contentType) {
//            this.bytes = bytes;
//            this.originalFilename = originalFilename;
//            this.contentType = contentType;
//        }
//
//        @Override
//        public String getName() {
//            return originalFilename;
//        }
//
//        @Override
//        public String getOriginalFilename() {
//            return originalFilename;
//        }
//
//        @Override
//        public String getContentType() {
//            return contentType;
//        }
//
//        @Override
//        public boolean isEmpty() {
//            return bytes == null || bytes.length == 0;
//        }
//
//        @Override
//        public long getSize() {
//            return bytes == null ? 0 : bytes.length;
//        }
//
//        @Override
//        public byte[] getBytes() {
//            return bytes;
//        }
//
//        @Override
//        public InputStream getInputStream() {
//            return new ByteArrayInputStream(bytes);
//        }
//
//        @Override
//        public void transferTo(File dest) throws IOException {
//            try (FileOutputStream fos = new FileOutputStream(dest)) {
//                fos.write(bytes);
//            }
//        }
//    }

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