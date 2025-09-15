package com.spring.jwt.dto;

import com.spring.jwt.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO1 {
    private Long studentId;
    private String name;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String studentcol;
    private String studentcol1;
    private String studentClass;
    private Long userId;

    public static UserProfileDTO1 fromEntity(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        UserProfileDTO1 dto = new UserProfileDTO1();
        dto.setStudentId(userProfile.getUserProfileId());
        dto.setName(userProfile.getName());
        dto.setLastName(userProfile.getLastName());
        dto.setDateOfBirth(userProfile.getDateOfBirth());
        dto.setAddress(userProfile.getAddress());
        dto.setStudentcol(userProfile.getStudentcol());
        dto.setStudentcol1(userProfile.getStudentcol1());
        dto.setStudentClass(userProfile.getStudentClass());

        // get User’s ID from relation
        if (userProfile.getUser() != null) {
            dto.setUserId(userProfile.getUser().getId());
        }

        return dto;
    }
}
