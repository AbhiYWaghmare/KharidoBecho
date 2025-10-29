package com.spring.jwt.utils.Colours.service;

import com.spring.jwt.utils.Colours.dto.ColourDto;
import com.spring.jwt.utils.Colours.entity.Colour;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ColourService {

    Colour createColour(ColourDto colourDto);

    Colour updateColour(Long colourId, ColourDto colourDto);

    List<Colour> getAllColours();

    void deleteColourById(Long colourId);
}
