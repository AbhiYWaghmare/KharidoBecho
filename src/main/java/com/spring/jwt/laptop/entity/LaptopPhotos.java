package com.spring.jwt.laptop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "laptopPhoto")
public class LaptopPhotos {

    @Id
    @Column(name = "laptop_photo_id" ,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoId;

    @Column(name = "laptop_id",nullable = false)
    private Integer laptopId;

    @Column(name = "photoLink",nullable = false)
    private String photo_link;

    @Column(name = "photo_type",nullable = false)
    private String type;

    @Column(name = "public_id",nullable = false)
    private String publicId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "laptop_id",nullable = false)
//    private Laptop laptop;
}
