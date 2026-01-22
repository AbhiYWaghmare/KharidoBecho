package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.RamOption;
import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class StorageOptionConverter extends DbEnumConverter<StorageOption> {
    public StorageOptionConverter() {
        super(StorageOption.class);
    }
    
}
