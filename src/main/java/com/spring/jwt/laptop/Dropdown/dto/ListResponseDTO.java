package com.spring.jwt.laptop.Dropdown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListResponseDTO {
    private String message;
    private List<?> list;

    private String exception;

}
