package com.spring.jwt.car.entity;

import com.spring.jwt.entity.Car;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car_photo")
@Builder
public class CarPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_photo_id", nullable = false)
    private Integer photoId; // primary key of this photo

//    @Column(name = "photo_link", nullable = false)
//    private String photoLink;

//    @Column(name = "photo_type", nullable = false)
//    private String type;

    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id") // FK column in car_photo table referencing car table
    private Car car;
}
