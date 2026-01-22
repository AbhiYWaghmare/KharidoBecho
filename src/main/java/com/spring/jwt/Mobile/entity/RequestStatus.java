package com.spring.jwt.Mobile.entity;

public enum RequestStatus {
    PENDING,          // Buyer created request
    IN_NEGOTIATION,   // Seller accepted (Socket.IO active)
    REJECTED,         // Seller rejected
    COMPLETED         // Deal completed
}

