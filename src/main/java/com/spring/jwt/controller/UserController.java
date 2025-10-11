package com.spring.jwt.controller;

import com.spring.jwt.dto.ResetPassword;
import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.dto.UserUpdateRequest;
import com.spring.jwt.dto.UserProfileDTO;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.User;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.EncryptionUtil;
import com.spring.jwt.utils.ErrorResponseDto;
import com.spring.jwt.utils.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "User Management API",
        description = "APIs for user registration, profile management, and authentication operations"
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@CrossOrigin(origins = "${app.cors.allowed-origins}", maxAge = 3600)
public class UserController {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final JwtService jwtService;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Value("${app.url.password-reset}")
    private String passwordResetUrl;

    // =================================
    // Register User According to Role
    // ==================================

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        BaseResponseDTO response = userService.registerAccount(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ======================================
    // Password reset endpoints
    // ======================================
    @PostMapping("/password/forgot")
    public ResponseEntity<ResponseDto> requestPasswordReset(
            @RequestParam @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
            HttpServletRequest request) {
        ResponseDto response = userService.handleForgotPassword(email, request.getServerName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/password/reset")
    public ResponseEntity<String> getResetPasswordPage(@RequestParam @NotBlank(message = "Token is required") String token) {
        try {
            if (!userService.validateResetToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
            }
            ClassPathResource resource = new ClassPathResource("templates/reset-password.html");
            String htmlContent = Files.readString(Paths.get(resource.getURI()), StandardCharsets.UTF_8);
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
        } catch (IOException e) {
            log.error("Error loading reset password template", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading password reset form");
        }
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ResponseDto> resetPassword(@Valid @RequestBody ResetPassword request) {
        ResponseDto response = userService.processPasswordUpdate(request);
        return ResponseEntity.ok(response);
    }

    // =======================
    // Users - Paginated list
    // GET /api/v1/users/getAllUsers?page=0&size=10&role=BUYER
    // =======================

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseAllUsersDto> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") int size,
            @RequestParam(required = false) String role) {

        Page<UserDTO> userPage = userService.getAllUsers(page, size, role);

        ResponseAllUsersDto response = buildPagedResponse(userPage);
        return ResponseEntity.ok(response);
    }

    // =======================
    // Get single user by id
    // =======================
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable @Min(value = 1, message = "Invalid user ID") Long id) {
        UserDTO user = userService.getUserById(id);
        try {
            if (user.getFirstName() != null) user.setFirstName(encryptionUtil.decrypt(user.getFirstName()));
            if (user.getLastName() != null) user.setLastName(encryptionUtil.decrypt(user.getLastName()));
            if (user.getAddress() != null) user.setAddress(encryptionUtil.decrypt(user.getAddress()));
        } catch (Exception e) {
            log.error("Error decrypting user data: {}", e.getMessage());
        }
        return ResponseEntity.ok(user);
    }



    // =======================
    // Profile endpoints (unchanged)
    // =======================
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable @Min(value = 1) Long id) {
        UserProfileDTO profile = userService.getUserProfileById(id);
        try {
            UserDTO user = profile.getUser();
            if (user != null) {
                if (user.getFirstName() != null) user.setFirstName(encryptionUtil.decrypt(user.getFirstName()));
                if (user.getLastName() != null) user.setLastName(encryptionUtil.decrypt(user.getLastName()));
                if (user.getAddress() != null) user.setAddress(encryptionUtil.decrypt(user.getAddress()));
            }
        } catch (Exception e) {
            log.error("Error decrypting user data: {}", e.getMessage());
        }
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        UserProfileDTO profile = userService.getCurrentUserProfile();
        try {
            UserDTO user = profile.getUser();
            if (user != null) {
                if (user.getFirstName() != null) user.setFirstName(encryptionUtil.decrypt(user.getFirstName()));
                if (user.getLastName() != null) user.setLastName(encryptionUtil.decrypt(user.getLastName()));
                if (user.getAddress() != null) user.setAddress(encryptionUtil.decrypt(user.getAddress()));
            }
        } catch (Exception e) {
            log.error("Error decrypting user data: {}", e.getMessage());
        }
        return ResponseEntity.ok(profile);
    }


    // =======================
    // Update user By ID
    // =======================
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable @Min(value = 1) Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // =======================
    // Device fingerprint
    // =======================
    @GetMapping("/security/device-fingerprint")
    public ResponseEntity<Map<String, Object>> checkDeviceFingerprint(HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));

            String storedFingerprint = user.getDeviceFingerprint();
            String currentFingerprint = jwtService.generateDeviceFingerprint(request);

            Map<String, Object> result = new HashMap<>();
            result.put("email", user.getEmail());
            result.put("lastLogin", user.getLastLogin());
            result.put("storedFingerprint", storedFingerprint != null ? storedFingerprint.substring(0, 8) + "..." : null);
            result.put("currentFingerprint", currentFingerprint != null ? currentFingerprint.substring(0, 8) + "..." : null);
            result.put("fingerprintsMatch", storedFingerprint != null && currentFingerprint != null && storedFingerprint.equals(currentFingerprint));

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error checking device fingerprint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error checking device fingerprint: " + e.getMessage()));
        }
    }



    // =======================
    // Buyers - paginated list
    // GET /api/v1/users/buyers?page=0&size=10
    // =======================
//    @GetMapping("/getAllBuyers")
//    public ResponseEntity<ResponseAllUsersDto> getAllBuyers(
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            @RequestParam(defaultValue = "10") @Min(1) int size) {
//
//        Page<UserDTO> userPage = userService.getAllUsers(page, size, "BUYER");
//        return ResponseEntity.ok(buildPagedResponse(userPage));
//    }
//
//    @GetMapping("/buyer/{userId}")
//    public ResponseEntity<Buyer> getBuyerByUserId(@PathVariable Long userId) {
//        Buyer buyer = buyerRepository.findByUser_Id(userId);
//        if (buyer == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(buyer);
//    }
//
//
//
//    // =======================
//    // Sellers - paginated list
//    // GET /api/v1/users/sellers?page=0&size=10
//    // =======================
//    @GetMapping("/getAllSellers")
//    public ResponseEntity<ResponseAllUsersDto> getAllSellers(
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            @RequestParam(defaultValue = "10") @Min(1) int size) {
//
//        Page<UserDTO> userPage = userService.getAllUsers(page, size, "SELLER");
//        return ResponseEntity.ok(buildPagedResponse(userPage));
//    }
//
//    @GetMapping("/seller/{userId}")
//    public ResponseEntity<Seller> getSellerByUserId(@PathVariable Long userId) {
//        Seller seller = sellerRepository.findByUser_Id(userId);
//        if (seller == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(seller);
//    }

    // =======================
    // Delete (soft) user
    // DELETE /api/v1/users/{id}
    // =======================

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }


    private ResponseAllUsersDto buildPagedResponse(Page<UserDTO> userPage) {
        List<UserDTO> decryptedUsers = userPage.getContent().stream()
                .map(u -> {
                    try {
                        if (u.getFirstName() != null) u.setFirstName(encryptionUtil.decrypt(u.getFirstName()));
                        if (u.getLastName() != null) u.setLastName(encryptionUtil.decrypt(u.getLastName()));
                        if (u.getAddress() != null) u.setAddress(encryptionUtil.decrypt(u.getAddress()));
                    } catch (Exception e) {
                        log.error("Error decrypting user data: {}", e.getMessage());
                    }
                    return u;
                })
                .toList();

        ResponseAllUsersDto response = new ResponseAllUsersDto("success", decryptedUsers);
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalElements(userPage.getTotalElements());
        response.setPageSize(userPage.getSize());
        response.setCurrentPage(userPage.getNumber());
        response.setFirst(userPage.isFirst());
        response.setLast(userPage.isLast());
        return response;
    }
}
