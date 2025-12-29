package com.spring.jwt.Mobile.entity;

import com.spring.jwt.entity.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mobiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mobile_id")
    private Long mobileId;

    @Column(nullable = false)
    private String title;

    @Column(length = 65535, columnDefinition = "TEXT")
    @Lob
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean negotiable = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "mobile_condition",nullable = false)
    private Condition condition;

    //    private String brand;
//    private String model;
    private String color;
    private Integer yearOfPurchase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private MobileModel model;


    // Link With Seller
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @OneToMany(mappedBy = "mobile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MobileImage> images = new ArrayList<>();

    // soft delete
    private boolean deleted = false;
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public enum Condition { NEW, USED, REFURBISHED }
    public enum Status { ACTIVE, SOLD, EXPIRED, DELETED }
}