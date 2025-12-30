package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.ProcessorBrand;
import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ProcessorBrandConverter extends DbEnumConverter<ProcessorBrand> {
    public ProcessorBrandConverter() {
        super(ProcessorBrand.class);
    }
    
}
