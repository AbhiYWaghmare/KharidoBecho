package com.spring.jwt.laptop.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class LaptopRequestCreateDTO {
    @NotNull(message = "Laptop ID is required")
    @Positive(message = "Laptop ID must be a positive number")
    private Long laptopId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerUserId;

    @NotBlank(message = "Message is required")
    @Size(max = 50, message = "Message field cannot exceed 50 characters")
    @Pattern(
            regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Zs}]+$",
            message = "Message contains invalid characters"
    )
    private String message;

    @NotNull(message = "Booking date is required")
    @Future(message = "Booking date must be in the future")
    private LocalDate bookingDate;
}
