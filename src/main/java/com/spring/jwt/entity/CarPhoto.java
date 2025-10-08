//package com.spring.jwt.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "carphoto")
//public class CarPhoto {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "car_photo_id", nullable = false)
//    private Integer photoId;
//
//    @Column(name = "photo_link", nullable = false)
//    private String photoLink;
//
//    @Column(name = "photo_type", nullable = false)
//    private String type;
//
//    @Column(name = "public_id", nullable = false)
//    private String publicId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "car_id")
//    private Car car;
//}