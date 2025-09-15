package com.spring.jwt.service;

import com.spring.jwt.dto.ResetPassword;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.dto.UserProfileDTO;
import com.spring.jwt.dto.UserUpdateRequest;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.ResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {
    BaseResponseDTO registerAccount(UserDTO userDTO);

    ResponseDto forgotPass(String email, String resetPasswordLink, String domain) throws UserNotFoundExceptions;

    ResponseDto handleForgotPassword(String email, String domain);

    void updateResetPassword(String token, String email);

    boolean validateResetToken(String token);

    boolean isSameAsOldPassword(String token, String newPassword);

    ResponseDto updatePassword(String token, String newPassword);

    ResponseDto processPasswordUpdate(ResetPassword resetRequest);

    // ✅ Only one version of getAllUsers should exist!
    Page<UserDTO> getAllUsers(int pageNo, int pageSize, String role);

    UserDTO getUserById(Long id);

    UserProfileDTO getUserProfileById(Long id);

    UserProfileDTO getCurrentUserProfile();

    UserDTO updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
