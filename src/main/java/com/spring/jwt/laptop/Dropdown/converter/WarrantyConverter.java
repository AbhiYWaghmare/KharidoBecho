package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import com.spring.jwt.laptop.Dropdown.model.Warranty;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class WarrantyConverter extends DbEnumConverter<Warranty> {
    public WarrantyConverter() {
        super(Warranty.class);
    }
    
}
