package com.spring.jwt.utils.Colours.service.impl;

import com.spring.jwt.utils.Colours.dto.ColourDto;
import com.spring.jwt.utils.Colours.entity.Colour;
import com.spring.jwt.exception.colour.ColourNotFoundException;
import com.spring.jwt.utils.Colours.repository.ColourRepository;
import com.spring.jwt.utils.Colours.service.ColourService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColourServiceImpl implements ColourService {

    private final ColourRepository colourRepository;
    @Override
    public Colour createColour(ColourDto colourDto) {

        Colour colour = new Colour();
        colour.setColourName(colourDto.getColourName());
        colour.setHexCode(colourDto.getHexCode());
        return colourRepository.save(colour);
    }

    @Override
    public Colour updateColour(Long colourId, ColourDto colourDto) {
        Colour existingColour = colourRepository.findById(colourId)
                .orElseThrow(()-> new ColourNotFoundException("Colour not found with id " +colourId));

        existingColour.setColourName(colourDto.getColourName());
        existingColour.setHexCode(colourDto.getHexCode());
        return colourRepository.save(existingColour);
    }

    @Override
    public List<Colour> getAllColours() {

        return colourRepository.findAll();
    }

    @Override
    public void deleteColourById(Long colourId) {
        if(!colourRepository.existsById(colourId)){
            throw new ColourNotFoundException("Colour not found with id " +colourId);
        }

        colourRepository.deleteById(colourId);
    }
}
