package com.spring.jwt.Mobile.dto;


import lombok.Builder;
import lombok.Data;

//===========================================================================================//
//Creating this DTO for getting MobileId in response for Smooth Intigration with Frontend//
//===========================================================================================//


@Data
@Builder
public class MobileCreateResponseDTO {

    private String code ;
    private String message;
    private Long mobileId;
}
