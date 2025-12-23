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
<<<<<<< HEAD
=======

>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
