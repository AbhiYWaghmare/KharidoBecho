//package com.spring.jwt.car.mapper;
//
//import com.spring.jwt.car.dto.CarBookingResponseDTO;
//import com.spring.jwt.car.entity.CarBooking;
//
//public class CarBookingMapper {
//
//    public static CarBookingResponseDTO toDTO(CarBooking booking) {
//
//        CarBookingResponseDTO dto = new CarBookingResponseDTO();
//
//        dto.setBookingId(booking.getBookingId());
//        dto.setBookingStatus(booking.getBookingStatus().name());
//
//        if (booking.getCar() != null) {
//            dto.setCarId(booking.getCar().getCarId());
//        }
//
//        return dto;
//    }
//}
