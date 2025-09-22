////package com.spring.jwt.car.dto;
////
////import lombok.Getter;
////import lombok.Setter;
////
////@Getter
////@Setter
////public class CarResponseDTO {
////    private int id;
////    private String brand;
////    private String model;
////    private String variant;
////    private String fuelType;
////    private String transmission;
////    private int year;
////    private int price;
////    private String color;
////    private String city;
////    private String description;
////    private String status;
////    private boolean pendingApproval;
////    private long carPhotoId;
////    private String mainCarId;
////    private String carType;
////    private String userId; // mapped from User entity
////}
//package com.spring.jwt.car.dto;
//
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BaseResponseDTO<T> {
//    private String code;
//    private String message;
//    private T data;
//}
//
package com.spring.jwt.car.dto;

import lombok.Data;

@Data
public class BaseResponseDTO<T> {
    private String message;
    private T data;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }
}

