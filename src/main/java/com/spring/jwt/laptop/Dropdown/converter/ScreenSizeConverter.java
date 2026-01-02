package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.ScreenSize;
import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ScreenSizeConverter extends DbEnumConverter<ScreenSize> {
    public ScreenSizeConverter() {
        super(ScreenSize.class);
    }
    
}
