package com.spring.jwt.utils.Colours.controller;

import com.spring.jwt.utils.Colours.dto.ColourDto;
import com.spring.jwt.utils.Colours.dto.ColourResponseDTO;
import com.spring.jwt.utils.Colours.entity.Colour;
import com.spring.jwt.exception.colour.ColourAlreadyExistsException;
import com.spring.jwt.utils.Colours.repository.ColourRepository;
import com.spring.jwt.utils.Colours.service.ColourService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colours")
@RequiredArgsConstructor
public class ColourController {

    private final ColourRepository colourRepository;
    private final ColourService colourService;

    @PostMapping("/createColour")
    public ResponseEntity<ColourResponseDTO> createColours(@Valid @RequestBody ColourDto colourDto,HttpServletRequest request){
        if (colourRepository.existsByColourName(colourDto.getColourName())) {
            throw new ColourAlreadyExistsException("Colour already exists with name: " + colourDto.getColourName());
        }

        Colour colour = colourService.createColour(colourDto);
        String apiPath = request.getRequestURI();
        Long colourId = colour.getColourId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ColourResponseDTO(
                        "success",
                        "Colour added successfully",
                        "CREATED",
                        200,
                        LocalDateTime.now(),
                        "NULL",
                        apiPath,
                        colourId));
    }

    @PatchMapping("/updateColour")
    public ResponseEntity<ColourResponseDTO> updateColour(
            @RequestParam Long colourId,
            @RequestBody ColourDto colourDto,
            HttpServletRequest request){
        Colour colour = colourService.updateColour(colourId,colourDto);
        String apiPath = request.getRequestURI();
        Long colour_Id = colour.getColourId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ColourResponseDTO(
                        "success",
                        "Colour updated successfully",
                        "UPDATE",
                        200,
                        LocalDateTime.now(),
                        "NULL",
                        apiPath,
                        colourId));

    }

    @GetMapping("/getAllColours")
    public ResponseEntity<List<Colour>> getAllColours(){
        return ResponseEntity.ok(colourService.getAllColours());
    }

    @DeleteMapping("/deleteColour")
    public ResponseEntity<Map<String, String>> deleteColourById(@RequestParam @Min(1) Long colourId) {
        colourService.deleteColourById(colourId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Colour deleted successfully");
        return ResponseEntity.ok(response);
    }

}
