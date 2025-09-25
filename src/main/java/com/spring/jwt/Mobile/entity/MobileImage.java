package com.spring.jwt.Mobile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mobile_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;























    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_id", nullable = false)
    private Mobile mobile;
}
