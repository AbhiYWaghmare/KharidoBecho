package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.mapper.UserMapper;
import com.spring.jwt.repository.*;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.DataMaskingUtils;
import com.spring.jwt.utils.EmailService;
import com.spring.jwt.utils.EmailVerificationService.EmailVerification;
import com.spring.jwt.utils.EmailVerificationService.EmailVerificationRepo;
import com.spring.jwt.utils.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmailVerificationRepo emailVerificationRepo;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.url.password-reset}")
    private String passwordResetUrl;

    // =======================
    // ACCOUNT REGISTRATION
    // =======================

    @Override
    @Transactional
    public BaseResponseDTO registerAccount(UserDTO userDTO) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(userDTO);

        User user = insertUser(userDTO);

        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setMessage("Account Created Successfully !!");

        return response;
    }

    @Transactional
    private User insertUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setAddress(userDTO.getAddress());
        user.setEmailVerified(true);

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(userDTO.getRole());
        if (role != null) roles.add(role);

        user.setRoles(roles);
        user = userRepository.save(user);

        if (role != null) {
            switch (role.getName()) {
                case "USER":
                    createUserProfile(user, userDTO);
                    break;
                case "BUYER":
                    createBuyer(user);
                    break;
                case "SELLER":
                    createSeller(user);
                    break;
            }
        }

        return user;
    }

    private void createUserProfile(User user, UserDTO userDTO) {
        UserProfile profile = new UserProfile();
        profile.setName(userDTO.getFirstName());
        profile.setLastName(userDTO.getLastName());
        profile.setDateOfBirth(userDTO.getDateOfBirth());
        profile.setAddress(userDTO.getAddress());
//        profile.setStudentcol(userDTO.getStudentcol());
//        profile.setStudentcol1(userDTO.getStudentcol1());
//        profile.setStudentClass(userDTO.getStudentClass());
        profile.setUser(user);
        userProfileRepository.save(profile);
        log.info("Created profile for user ID: {}", user.getId());
    }

    private void createBuyer(User user) {
        Buyer buyer = new Buyer();
        buyer.setUser(user);
        buyerRepository.save(buyer);
        log.info("Created buyer profile for user ID: {}", user.getId());
    }

    private void createSeller(User user) {
        Seller seller = new Seller();
        seller.setUser(user);
        sellerRepository.save(seller);
        log.info("Created seller profile for user ID: {}", user.getId());
    }

    private void validateAccount(UserDTO userDTO) {
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not be empty");
        }

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Email is already registered !!");
        }

        Optional<User> mobileNumber = userRepository.findByMobileNumber(userDTO.getMobileNumber());
        if (mobileNumber.isPresent()) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Mobile Number is already registered !!");
        }

        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        if (!roles.contains(userDTO.getRole())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }
    }

    // =======================
    // PASSWORD RESET
    // =======================

    @Override
    public ResponseDto forgotPass(String email, String resetPasswordLink, String domain) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundExceptions("User not found");
        emailService.sendResetPasswordEmail(email, resetPasswordLink, domain);
        return new ResponseDto(HttpStatus.OK.toString(), "Email sent");
    }

    @Override
    @Transactional
    public ResponseDto handleForgotPassword(String email, String domain) {
        if (email == null || email.isEmpty()) return new ResponseDto("Unsuccessful", "Email is required");
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundExceptions("User not found with email: " + email);

        String token = RandomStringUtils.randomAlphanumeric(64);
        updateResetPassword(token, email);

        String resetPasswordLink = passwordResetUrl + "?token=" + token;
        emailService.sendResetPasswordEmail(email, resetPasswordLink, domain);

        return new ResponseDto("Successful", "Password reset instructions sent to your email");
    }

    @Override
    @Transactional
    public void updateResetPassword(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundExceptions("User not found with email: " + email);

        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseDto updatePassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null || user.getResetPasswordTokenExpiry() == null ||
                LocalDateTime.now().isAfter(user.getResetPasswordTokenExpiry())) {
            throw new UserNotFoundExceptions("Invalid or expired token");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);

        return new ResponseDto(HttpStatus.OK.toString(), "Password reset successful");
    }

    @Override
    public ResponseDto processPasswordUpdate(ResetPassword resetRequest) {
        return null;
    }

    @Override
    public boolean validateResetToken(String token) {
        User user = userRepository.findByResetPasswordToken(token);
        return user != null && user.getResetPasswordTokenExpiry() != null
                && LocalDateTime.now().isBefore(user.getResetPasswordTokenExpiry());
    }

    @Override
    public boolean isSameAsOldPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) throw new UserNotFoundExceptions("Invalid or expired token");
        return passwordEncoder.matches(newPassword, user.getPassword());
    }

    // =======================
    // USER CRUD & PROFILE
    // =======================

    @Override
    public Page<UserDTO> getAllUsers(int pageNo, int pageSize, String role) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = (role != null && !role.isBlank()) ?
                userRepository.findByRoleNameAndDeletedFalse(role, pageable) :
                userRepository.findByDeletedFalse(pageable);
        return users.map(user -> populateRoleSpecificData(user, userMapper.toDTO(user)));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));
        return populateRoleSpecificData(user, userMapper.toDTO(user));
    }

    private UserDTO populateRoleSpecificData(User user, UserDTO userDTO) {
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        userDTO.setRoles(roles);

        if (roles.contains("USER")) {
            UserProfile profile = userProfileRepository.findByUserId(user.getId());
            if (profile != null) {
                userDTO.setRole("USER");
                userDTO.setDateOfBirth(profile.getDateOfBirth());
//                userDTO.setStudentcol(profile.getStudentcol());
//                userDTO.setStudentcol1(profile.getStudentcol1());
//                userDTO.setStudentClass(profile.getStudentClass());
            }
        }
        if (roles.contains("BUYER")) {
            Buyer buyer = buyerRepository.findByUser_Id(user.getId());
            if (buyer != null) userDTO.setRole("BUYER");
        }
        if (roles.contains("SELLER")) {
            Seller seller = sellerRepository.findByUser_Id(user.getId());
            if (seller != null) userDTO.setRole("SELLER");
        }
        return userDTO;
    }

    @Override
    public UserDTO updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName().trim());
        if (request.getLastName() != null) user.setLastName(request.getLastName().trim());
        if (request.getAddress() != null) user.setAddress(request.getAddress().trim());
        if (request.getMobileNumber() != null) user.setMobileNumber(Long.valueOf(request.getMobileNumber()));

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserProfileDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));
        return buildUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new BaseException("401", "User not authenticated");
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundExceptions("User not found with email: " + email);
        return buildUserProfileDTO(user);
    }

    private UserProfileDTO buildUserProfileDTO(User user) {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUser(userMapper.toDTO(user));
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        profileDTO.setRoles(roles);
        if (roles.contains("USER")) {
            UserProfile profile = userProfileRepository.findByUserId(user.getId());
            if (profile != null) profileDTO.setUserProfileDTO1(UserProfileDTO1.fromEntity(profile));
        }
        return profileDTO;
    }



    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        System.err.println("Local time " + now);

        // Soft delete User

        user.setDeleted(true);
        user.setDeletedAt(now);
        userRepository.save(user);


        // Soft delete Buyer if exists

        Buyer buyer = buyerRepository.findByUser_Id(user.getId());
        if (buyer != null) {
            buyer.setDeleted(true);
            buyer.setDeletedAt(now);
            buyerRepository.save(buyer);
        }

        // Soft delete Seller if exists

        Seller seller = sellerRepository.findByUser_Id(user.getId());
        if (seller != null) {
            seller.setDeleted(true);
            seller.setDeletedAt(now);
            sellerRepository.save(seller);
        }
        log.info("Soft deleted user and related buyer/seller with ID: {}", id);

    }





/// ////////////////////////////////////////////////////

    // Use this in Future if We want hard Delete

////////////////////////////////////////////////////////

//    @Override
//    @Transactional
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));
//
//        user.setDeleted(true);
//        user.setDeletedAt(LocalDateTime.now());
//        userRepository.save(user);
//    }



//    private void deleteBuyer(User user) {
//        Buyer buyer = buyerRepository.findByUser_Id(user.getId());
//        if (buyer != null) {
//            buyerRepository.delete(buyer);
//            log.info("Deleted buyer profile for user ID: {}", user.getId());
//        }
//    }
//
//    private void deleteSeller(User user) {
//        Seller seller = sellerRepository.findByUser_Id(user.getId());
//        if (seller != null) {
//            sellerRepository.delete(seller);
//            log.info("Deleted seller profile for user ID: {}", user.getId());
//        }
//    }


}
