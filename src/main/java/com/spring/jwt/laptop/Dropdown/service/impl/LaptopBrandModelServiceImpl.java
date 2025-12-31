package com.spring.jwt.laptop.Dropdown.service.impl;

import com.spring.jwt.laptop.Dropdown.dto.LaptopBrandModelDTO;
import com.spring.jwt.laptop.Dropdown.dto.OnlyBrandDTO;
import com.spring.jwt.laptop.Dropdown.entity.LaptopBrandModel;
import com.spring.jwt.laptop.Dropdown.repository.LaptopBrandModelRepository;
import com.spring.jwt.laptop.Dropdown.service.LaptopBrandModelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LaptopBrandModelServiceImpl implements LaptopBrandModelService {
    private final LaptopBrandModelRepository repository;

    @Override
    public LaptopBrandModelDTO add(LaptopBrandModelDTO dto) {

        repository.findByBrandAndModel(dto.getBrand(), dto.getModel())
                .ifPresent(e -> {
                    throw new RuntimeException(
                            "Brand & Model already exists");
                });

        LaptopBrandModel entity = new LaptopBrandModel();
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());

        repository.save(entity);
        return dto;
    }

    @Override
    public List<LaptopBrandModelDTO> getAll(int pageNo, int pageSize) {

        Pageable pageable =
                PageRequest.of(pageNo, pageSize, Sort.by("id").descending());

        return repository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<OnlyBrandDTO> getOnlyBrands() {
        return repository.findDistinctBrands()
                .stream()
                .map(OnlyBrandDTO::new)
                .toList();
    }

    @Override
    public List<LaptopBrandModelDTO> getModelsByBrand(String brand) {

        List<LaptopBrandModel> models = repository.findByBrand(brand);

        if (models.isEmpty()) {
            throw new RuntimeException(
                    "No models found for brand: " + brand);
        }

        return models.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public String update(Long id, LaptopBrandModelDTO dto) {

        LaptopBrandModel entity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Not found"));

        if (dto.getBrand() != null)
            entity.setBrand(dto.getBrand());

        if (dto.getModel() != null)
            entity.setModel(dto.getModel());

        repository.save(entity);
        return "Updated successfully";
    }

    @Override
    public String delete(Long id) {
        repository.deleteById(id);
        return "Deleted successfully";
    }

    private LaptopBrandModelDTO toDto(LaptopBrandModel e) {
        LaptopBrandModelDTO dto = new LaptopBrandModelDTO();
        dto.setId(e.getId());
        dto.setBrand(e.getBrand());
        dto.setModel(e.getModel());
        return dto;
    }
}
