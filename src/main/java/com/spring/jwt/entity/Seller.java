package com.spring.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long sellerId;  // own primary key

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
            "roles"
    })
    private User user;
}
