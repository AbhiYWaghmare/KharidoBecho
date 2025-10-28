package com.spring.jwt.Mobile.Mapper;

import com.spring.jwt.Mobile.dto.MobileRequestDTO;
import com.spring.jwt.Mobile.dto.MobileResponseDTO;
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
        dto.setBrand(m.getBrand());
        dto.setModel(m.getModel());
        dto.setColor(m.getColor());
        dto.setYearOfPurchase(m.getYearOfPurchase());
        dto.setStatus(m.getStatus() != null ? m.getStatus().name() : null);
        dto.setCreatedAt(m.getCreatedAt());
        dto.setUpdatedAt(m.getUpdatedAt());
        dto.setSellerId(m.getSeller() != null ? m.getSeller().getSellerId() : null);
        dto.setImages(m.getImages().stream().map(MobileImage::getImageUrl).collect(Collectors.toList()));
        return dto;
    }

    public static void updateFromRequest(Mobile m, MobileRequestDTO r) {
        if (r.getTitle() != null) m.setTitle(r.getTitle());
        if (r.getDescription() != null) m.setDescription(r.getDescription());
        if (r.getPrice() != null) m.setPrice(r.getPrice());
        if (r.getNegotiable() != null) m.setNegotiable(r.getNegotiable());
        if (r.getCondition() != null) m.setCondition(Mobile.Condition.valueOf(r.getCondition()));
        if (r.getBrand() != null) m.setBrand(r.getBrand());
        if (r.getModel() != null) m.setModel(r.getModel());
        if (r.getColor() != null) m.setColor(r.getColor());
        if (r.getYearOfPurchase() != null) m.setYearOfPurchase(r.getYearOfPurchase());
    }
}
