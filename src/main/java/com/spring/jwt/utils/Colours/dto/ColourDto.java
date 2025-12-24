package com.spring.jwt.utils.Colours.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColourDto {

    @NotBlank(message = "Colour Name is required")
    @Size(max = 50, message = "Colour cannot exceed 50 characters")
    @Pattern(
            regexp = "^\\s*(?=.*\\p{L})[\\p{L}\\s\\-\\.]+\\s*$",
            message = "Colour must contain letters only and cannot have digits or special characters"
    )
    private String colourName;

    private String hexCode;
}
