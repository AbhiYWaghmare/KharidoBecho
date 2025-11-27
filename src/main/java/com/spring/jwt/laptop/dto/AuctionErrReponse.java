package com.spring.jwt.laptop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuctionErrReponse {
    private String apiPath;
    private String status;       // success / error
    private String message;
    private String code;         // NOT_FOUND / BAD_REQUEST / etc
    private Integer statusCode;
    private LocalDateTime timeStamp;
    private String exception;
}
