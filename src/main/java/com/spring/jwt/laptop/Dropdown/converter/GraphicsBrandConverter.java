package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.GraphicsBrand;
import com.spring.jwt.laptop.Dropdown.model.StorageOption;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GraphicsBrandConverter extends DbEnumConverter<GraphicsBrand> {
    public GraphicsBrandConverter() {
        super(GraphicsBrand.class);
    }
    
}
