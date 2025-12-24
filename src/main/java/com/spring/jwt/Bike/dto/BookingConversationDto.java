package com.spring.jwt.Bike.dto;

import lombok.Data;


import java.time.OffsetDateTime;

    @Data
    public class BookingConversationDto {

        private Long userId;
        private String message;
        private OffsetDateTime timestamp;
        private String senderType;
    }

