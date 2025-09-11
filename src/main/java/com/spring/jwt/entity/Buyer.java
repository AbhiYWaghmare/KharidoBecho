package com.spring.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buyers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {

    // Primary key is the same as User.user_id (share PK)
    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId // use the same primary key value as User
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
