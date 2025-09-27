package com.spring.jwt.car.entity;

import com.spring.jwt.entity.Car;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cloudinary_images")
public class CloudinaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;
    private String url;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")  // foreign key column in cloudinary_images table
    private Car car;

}
