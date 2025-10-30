package com.spring.jwt.laptop.model;

public enum Status {
    ACTIVE("Active"),
    AVAILABLE("Available"),
    DELETED("Deleted"),
    DEACTIVATE("Deactivated"),
    PENDINGREQUEST("Pending"),
    SOLD("Sold");


    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static com.spring.jwt.entity.Status fromString(String status) {
        for (com.spring.jwt.entity.Status s : com.spring.jwt.entity.Status.values()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }

}
