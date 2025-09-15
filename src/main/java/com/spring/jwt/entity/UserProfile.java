package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    private String name;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String studentcol;
    private String studentcol1;
    private String studentClass;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;   // <-- must match "mappedBy = 'user'" in User.java

    // ✅ Soft delete fields
    private boolean deleted = false;

    private LocalDateTime deletedAt;


}
