package com.spring.jwt.laptop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LaptopRequestCreateDTO {
    @NotNull(message = "Laptop ID is required")
    @Positive(message = "Laptop ID must be a positive number")
    private Long laptopId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerUserId;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 50, message = "Message must be between {min} and {max} characters long")
    @Pattern(
            regexp = "^[\\p{L}\\p{P}\\p{Zs}]+$",
            message = "Message can only contain letters, punctuation, and spaces (no digits allowed)"
    )
    private String message;


    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
    private LocalDate bookingDate;

}
