package com.spring.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;  // separate PK for sellers

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({
            "password",
            "resetPasswordToken",
            "resetPasswordTokenExpiry",
            "deviceFingerprint",
            "lastLogin",
            "loginAttempts",
            "accountLockedUntil",
            "roles",
            "buyer",
            "seller",
            "userProfile"
    })
    private User user;

    //  Soft delete fields
    private boolean deleted = false;

    private LocalDateTime deletedAt;

}
