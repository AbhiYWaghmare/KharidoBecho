             package com.spring.jwt.car.dto;

             import jakarta.validation.constraints.NotNull;
             import jakarta.validation.constraints.Positive;
             import jakarta.validation.constraints.Size;
             import lombok.Data;
             import lombok.Getter;
             import lombok.Setter;


//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class CarBookingDTO {
                 @NotNull(message = "carId is required")
                 @Positive(message = "carId must be a positive number")
                 private Long carId;

                 @NotNull(message = "buyerId is required")
                 @Positive(message = "buyerId must be a positive number")
                 private Long buyerId;

//                @NotNull(message = "buyerId is required")
//                @Positive(message = "buyerId must be a positive number")
//                private Long sellerId;

//                 @NotNull(message = "userId is required")
                 @Positive(message = "userId must be a positive number")
                 private Long userId;

                 @Size(max = 2000, message = "message cannot exceed 2000 characters")
                 private String message;

}
