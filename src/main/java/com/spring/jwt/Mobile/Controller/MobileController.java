package com.spring.jwt.Mobile.Controller;

import com.spring.jwt.Mobile.Services.MobileService;
import com.spring.jwt.Mobile.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//********************************************************//

//Author : Abhishek Waghmare
//Mobile Controller
//Date : 22/09/2025

//*******************************************************//

@RestController
@RequestMapping("/api/v1/mobiles")
@RequiredArgsConstructor
public class MobileController {

    private final MobileService mobileService;

    //To Add mobile for sell
    @PostMapping("/add")
    public ResponseEntity<MobileAddReqDTO> createMobile(@Valid @RequestBody MobileRequestDTO request) {

        // Convert condition to uppercase before mapping
        request.setCondition(request.getCondition().toUpperCase());

        MobileResponseDTO savedMobile = mobileService.createMobile(request);

        MobileAddReqDTO response = MobileAddReqDTO.builder()
                .code("201")
                .message("Mobile Added Successfully !!")
                .mobileId(savedMobile.getMobileId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //To get all mobiles
    @GetMapping("/getAllMobiles")
    public ResponseEntity<Page<MobileResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long sellerId
    ) {
        Page<MobileResponseDTO> p = mobileService.listMobiles(page, size, sellerId);
        return ResponseEntity.ok(p);
    }

    //To get mobile by ID
    @GetMapping("/{id}")
    public ResponseEntity<MobileResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(mobileService.getMobile(id));
    }

    //To update the details of mobile by id
    @PatchMapping("/update/{id}")
    public ResponseEntity<MobileResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MobileUpdateDTO req) {
        return ResponseEntity.ok(mobileService.updateMobile(id, req));
    }

    //To delete mobile by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id) {
        mobileService.softDeleteMobile(id);
        return ResponseEntity.ok(Map.of("status","success","message","Mobile soft-deleted"));
    }


}