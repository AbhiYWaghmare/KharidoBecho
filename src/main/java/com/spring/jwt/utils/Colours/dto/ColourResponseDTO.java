package com.spring.jwt.utils.Colours.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColourResponseDTO {
    private String status;
    private String message;
    private String code;
    private Integer statusCode;
    private LocalDateTime timeStamp;
    private String exception;
    private String apiPath;

    private Long colourId;

//    public ColourResponseDTO(Long colourId, String colourName, String hexCode) {
//    }
}
