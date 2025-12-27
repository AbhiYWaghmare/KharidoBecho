package com.spring.jwt.Mobile.Mapper;

import com.spring.jwt.Mobile.dto.MobileImageDTO;
import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
import com.spring.jwt.Mobile.dto.MobileUpdateDTO;
import com.spring.jwt.Mobile.entity.Mobile;
import com.spring.jwt.Mobile.entity.MobileImage;

import java.util.stream.Collectors;

public class MobileMapper {
    public static MobileResponseDTO toDTO(Mobile m) {
        if (m == null) return null;
        MobileResponseDTO dto = new MobileResponseDTO();
        dto.setMobileId(m.getMobileId());
        dto.setTitle(m.getTitle());
        dto.setDescription(m.getDescription());
        dto.setPrice(m.getPrice());
        dto.setNegotiable(m.getNegotiable());
        dto.setCondition(m.getCondition() != null ? m.getCondition().name() : null);
//        dto.setBrand(m.getBrand());
//        dto.setModel(m.getModel());
        dto.setBrand(
                m.getModel() != null ? m.getModel().getBrand().getName() : null
        );
        dto.setModel(
                m.getModel() != null ? m.getModel().getName() : null
        );

        dto.setColor(m.getColor());
        dto.setYearOfPurchase(m.getYearOfPurchase());
        dto.setStatus(m.getStatus() != null ? m.getStatus().name() : null);
        dto.setCreatedAt(m.getCreatedAt());
        dto.setUpdatedAt(m.getUpdatedAt());
        dto.setSellerId(m.getSeller() != null ? m.getSeller().getSellerId() : null);
//        dto.setImages(m.getImages().stream().map(MobileImage::getImageUrl).collect(Collectors.toList()));

        // changed part: map MobileImage -> MobileImageDTO (id + url)
        dto.setImages(
                m.getImages().stream()
                        .map(MobileMapper::toImageDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private static MobileImageDTO toImageDTO(MobileImage image) {
        MobileImageDTO dto = new MobileImageDTO();
        dto.setImageId(image.getImageId());
        dto.setImageUrl(image.getImageUrl());
        return dto;
    }

//    public static void updateFromRequest(Mobile m, MobileUpdateDTO r) {
//        if (r.getTitle() != null) m.setTitle(r.getTitle());
//        if (r.getDescription() != null) m.setDescription(r.getDescription());
//        if (r.getPrice() != null) m.setPrice(r.getPrice());
//        if (r.getNegotiable() != null) m.setNegotiable(r.getNegotiable());
//        if (r.getCondition() != null) m.setCondition(Mobile.Condition.valueOf(r.getCondition()));
//        if (r.getBrand() != null) m.setBrand(r.getBrand());
//        if (r.getModel() != null) m.setModel(r.getModel());
//        if (r.getColor() != null) m.setColor(r.getColor());
//        if (r.getYearOfPurchase() != null) m.setYearOfPurchase(r.getYearOfPurchase());
//    }

    public static void updateFromRequest(Mobile m, MobileRequestDTO req) {

        m.setTitle(req.getTitle());
        m.setDescription(req.getDescription());
        m.setPrice(req.getPrice());
        m.setNegotiable(req.getNegotiable());
//        m.setBrand(req.getBrand());
//        m.setModel(req.getModel());
        m.setColor(req.getColor());
        if (req.getCondition() != null) {
            m.setCondition(Mobile.Condition.valueOf(req.getCondition().toUpperCase()));
        }

        m.setYearOfPurchase(req.getYearOfPurchase());
    }

    public static void updateFromRequest(Mobile m, MobileUpdateDTO req) {

        if (req.getTitle() != null) m.setTitle(req.getTitle());
        if (req.getDescription() != null) m.setDescription(req.getDescription());
        if (req.getPrice() != null) m.setPrice(req.getPrice());
        if (req.getNegotiable() != null) m.setNegotiable(req.getNegotiable());
//        if (req.getBrand() != null) m.setBrand(req.getBrand());
//        if (req.getModel() != null) m.setModel(req.getModel());
        if (req.getColor() != null) m.setColor(req.getColor());

        if (req.getCondition() != null) {
            m.setCondition(Mobile.Condition.valueOf(req.getCondition().toUpperCase()));
        }

        if (req.getYearOfPurchase() != null) {
            m.setYearOfPurchase(req.getYearOfPurchase());
        }
    }

}
