package com.spring.jwt.Mobile.dto;

import com.spring.jwt.Mobile.entity.ConversationMessage;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class MobileRequestResponseDTO {
    private Long requestId;
    private Long mobileId;
    private Long buyerId;
    private Long sellerId;
<<<<<<< HEAD
=======
    private String buyerName;
    private String sellerName;

>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

//    private String conversationJson; // Raw JSON from DB
    private List<ConversationMessage> conversation; // Parsed JSON for frontend
}
