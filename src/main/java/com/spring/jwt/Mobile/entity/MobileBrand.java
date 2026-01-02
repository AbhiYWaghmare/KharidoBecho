package com.spring.jwt.Mobile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mobile_brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private boolean deleted = false;
}

