package com.spring.jwt.laptop.Dropdown.dto;

import lombok.Data;

import java.util.List;

@Data
public class LaptopMasterResponseDTO {
    private List<DropdownOptionDTO> brands;
    private List<DropdownOptionDTO> dealers;
    private List<DropdownOptionDTO> processorBrands;
    private List<DropdownOptionDTO> processors;
    private List<DropdownOptionDTO> memoryTypes;
    private List<DropdownOptionDTO> ram;
    private List<DropdownOptionDTO> storage;
    private List<DropdownOptionDTO> screenSizes;
    private List<DropdownOptionDTO> graphicsBrands;
    private List<DropdownOptionDTO> graphicsCards;
    private List<DropdownOptionDTO> usbPorts;
    private List<DropdownOptionDTO> colours;
}
