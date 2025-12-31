package com.spring.jwt.laptop.Dropdown.controller;

import com.spring.jwt.laptop.Dropdown.dto.LaptopMasterResponseDTO;
import com.spring.jwt.laptop.Dropdown.service.LaptopMasterDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/dropDown")
public class LaptopDropDownController {
    private final LaptopMasterDataService service;

    @GetMapping("/getAll")
    public LaptopMasterResponseDTO getAllMasterData() {

        LaptopMasterResponseDTO response = new LaptopMasterResponseDTO();

        response.setBrands(service.getBrands());
        response.setDealers(service.getDealers());
        response.setProcessorBrands(service.getProcessorBrands());
        response.setProcessors(service.getProcessors());
        response.setMemoryTypes(service.getMemoryTypes());
        response.setRam(service.getRamOptions());
        response.setStorage(service.getStorageOptions());
        response.setScreenSizes(service.getScreenSizes());
        response.setGraphicsBrands(service.getGraphicsBrands());
        response.setGraphicsCards(service.getGraphicsCards());
        response.setUsbPorts(service.getUsbPorts());
        response.setColours(service.getColours());

        return response;
    }
}
