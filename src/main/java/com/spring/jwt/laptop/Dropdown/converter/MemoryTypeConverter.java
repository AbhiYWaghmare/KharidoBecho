package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.MemoryType;
import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class MemoryTypeConverter extends DbEnumConverter<MemoryType> {
    public MemoryTypeConverter() {
        super(MemoryType.class);
    }
    
}
