package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.RamOption;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RamOptionConverter extends DbEnumConverter<RamOption> {
    public RamOptionConverter() {
        super(RamOption.class);
    }

}
