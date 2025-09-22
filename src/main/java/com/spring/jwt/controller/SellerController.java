package com.spring.jwt.controller;

import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.mobile.SellerNotFoundException;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.service.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;
    private final SellerRepository sellerRepository;

    @GetMapping("/getAllSellers")
    public ResponseEntity<ResponseAllUsersDto> getAllSellers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        Page<UserDTO> userPage = userService.getAllUsers(page, size, "SELLER");
        return ResponseEntity.ok(buildPagedResponse(userPage));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Seller> getSellerByUserId(@PathVariable Long userId) {
        Seller seller = sellerRepository.findByUser_Id(userId);
        if (seller == null) {
            throw new SellerNotFoundException(userId);
        }
        return ResponseEntity.ok(seller);
    }

    private ResponseAllUsersDto buildPagedResponse(Page<UserDTO> userPage) {
        ResponseAllUsersDto response = new ResponseAllUsersDto("success", userPage.getContent());
        response.setList(userPage.getContent());
        response.setCurrentPage(userPage.getNumber());
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        return response;
    }
}
