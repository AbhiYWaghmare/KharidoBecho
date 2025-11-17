package com.spring.jwt.Mobile.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MobileRequestCreateDTO {
    private Long mobileId;
    private Long buyerUserId; // user's id (from User table)
    private String message;   // optional initial message
}
