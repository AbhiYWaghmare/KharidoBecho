package com.spring.jwt.controller;

import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.exception.mobile.BuyerNotFoundException;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.service.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final UserService userService;
    private final BuyerRepository buyerRepository;

    @GetMapping("/getAllBuyers")
    public ResponseEntity<ResponseAllUsersDto> getAllBuyers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        Page<UserDTO> userPage = userService.getAllUsers(page, size, "BUYER");
        return ResponseEntity.ok(buildPagedResponse(userPage));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Buyer> getBuyerByUserId(@PathVariable Long userId) {
        Buyer buyer = buyerRepository.findByUser_Id(userId);
        if (buyer == null) {
            throw new BuyerNotFoundException(userId);
        }
        return ResponseEntity.ok(buyer);
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
