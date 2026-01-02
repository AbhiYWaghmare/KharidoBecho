package com.spring.jwt.laptop.Dropdown.service;


import com.spring.jwt.laptop.Dropdown.dto.LaptopBrandModelDTO;
import com.spring.jwt.laptop.Dropdown.dto.OnlyBrandDTO;

import java.util.List;

public interface LaptopBrandModelService {
    LaptopBrandModelDTO add(LaptopBrandModelDTO dto);

    List<LaptopBrandModelDTO> getAll(int pageNo, int pageSize);

    List<OnlyBrandDTO> getOnlyBrands();

    List<LaptopBrandModelDTO> getModelsByBrand(String brand);

    String update(Long id, LaptopBrandModelDTO dto);

    String delete(Long id);
}
