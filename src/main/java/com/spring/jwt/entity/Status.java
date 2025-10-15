package com.spring.jwt.entity;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("Pending"),
    ACTIVE("ACTIVE"),
    SOLD("Sold"),
    DELETED("Deleted"),
    DEACTIVATE("Deactivate"),
    AVAILABLE("Available");




    private final String status;

    Status(String status) {
        this.status = status;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }
}


