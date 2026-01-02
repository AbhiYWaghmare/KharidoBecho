package com.spring.jwt.Mobile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mobile_models",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "brand_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private MobileBrand brand;

    private boolean deleted = false;
}
