package com.spring.jwt.laptop.Dropdown.service;

import com.spring.jwt.laptop.Dropdown.dto.DropdownOptionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaptopMasterDataService {

    /* -------- BRAND -------- */
    public List<DropdownOptionDTO> getBrands() {
        return List.of(
                new DropdownOptionDTO("Apple", "APPLE"),
                new DropdownOptionDTO("Dell", "DELL"),
                new DropdownOptionDTO("HP", "HP"),
                new DropdownOptionDTO("Lenovo", "LENOVO"),
                new DropdownOptionDTO("ASUS", "ASUS"),
                new DropdownOptionDTO("Acer", "ACER")
        );
    }

    /* -------- DEALER -------- */
    public List<DropdownOptionDTO> getDealers() {
        return List.of(
                new DropdownOptionDTO("Amazon", "AMAZON"),
                new DropdownOptionDTO("Flipkart", "FLIPKART"),
                new DropdownOptionDTO("Croma", "CROMA"),
                new DropdownOptionDTO("Reliance Digital", "RELIANCE")
        );
    }

    /* -------- PROCESSOR BRAND -------- */
    public List<DropdownOptionDTO> getProcessorBrands() {
        return List.of(
                new DropdownOptionDTO("Intel", "INTEL"),
                new DropdownOptionDTO("AMD", "AMD"),
                new DropdownOptionDTO("Apple", "APPLE")
        );
    }

    /* -------- PROCESSOR -------- */
    public List<DropdownOptionDTO> getProcessors() {
        return List.of(
                new DropdownOptionDTO("Intel i3", "I3"),
                new DropdownOptionDTO("Intel i5", "I5"),
                new DropdownOptionDTO("Intel i7", "I7"),
                new DropdownOptionDTO("Ryzen 5", "RYZEN5"),
                new DropdownOptionDTO("Apple M1", "M1"),
                new DropdownOptionDTO("Apple M2", "M2")
        );
    }

    /* -------- MEMORY TYPE -------- */
    public List<DropdownOptionDTO> getMemoryTypes() {
        return List.of(
                new DropdownOptionDTO("DDR4", "DDR4"),
                new DropdownOptionDTO("DDR5", "DDR5"),
                new DropdownOptionDTO("LPDDR5", "LPDDR5")
        );
    }

    /* -------- RAM -------- */
    public List<DropdownOptionDTO> getRamOptions() {
        return List.of(
                new DropdownOptionDTO("8 GB", "8GB"),
                new DropdownOptionDTO("16 GB", "16GB"),
                new DropdownOptionDTO("32 GB", "32GB")
        );
    }

    /* -------- STORAGE -------- */
    public List<DropdownOptionDTO> getStorageOptions() {
        return List.of(
                new DropdownOptionDTO("256 GB SSD", "256_SSD"),
                new DropdownOptionDTO("512 GB SSD", "512_SSD"),
                new DropdownOptionDTO("1 TB SSD", "1TB_SSD")
        );
    }

    /* -------- SCREEN SIZE -------- */
    public List<DropdownOptionDTO> getScreenSizes() {
        return List.of(
                new DropdownOptionDTO("14 inch", "14"),
                new DropdownOptionDTO("15.6 inch", "15_6"),
                new DropdownOptionDTO("16 inch", "16")
        );
    }

    /* -------- GRAPHICS BRAND -------- */
    public List<DropdownOptionDTO> getGraphicsBrands() {
        return List.of(
                new DropdownOptionDTO("Intel", "INTEL"),
                new DropdownOptionDTO("NVIDIA", "NVIDIA"),
                new DropdownOptionDTO("AMD", "AMD"),
                new DropdownOptionDTO("Integrated", "INTEGRATED")
        );
    }

    /* -------- GRAPHICS CARD -------- */
    public List<DropdownOptionDTO> getGraphicsCards() {
        return List.of(
                new DropdownOptionDTO("Integrated Graphics", "INTEGRATED"),
                new DropdownOptionDTO("RTX 3050", "RTX_3050"),
                new DropdownOptionDTO("RTX 4060", "RTX_4060")
        );
    }

    /* -------- USB PORTS -------- */
    public List<DropdownOptionDTO> getUsbPorts() {
        return List.of(
                new DropdownOptionDTO("2", "2"),
                new DropdownOptionDTO("3", "3"),
                new DropdownOptionDTO("4", "4")
        );
    }

    /* -------- COLOURS -------- */
    public List<DropdownOptionDTO> getColours() {
        return List.of(
                new DropdownOptionDTO("Black", "BLACK"),
                new DropdownOptionDTO("Silver", "SILVER"),
                new DropdownOptionDTO("Grey", "GREY")
        );
    }
}
