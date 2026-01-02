package com.spring.jwt.laptop.controller;

import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
import com.spring.jwt.laptop.dto.LaptopAddReqDTO;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.entity.LaptopBooking;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//********************************************************//

//Author : Sudhir Lokade
//Laptop Controller
//Date : 22/09/2025

//*******************************************************//

@RestController
@RequestMapping("/api/laptops")
@RequiredArgsConstructor
public class LaptopController {
    private final LaptopRepository laptopRepository;
    private final LaptopService laptopService;
    LaptopBooking booking;
    LaptopResponseDTO laptopResponseDTO = new LaptopResponseDTO();



    //====================================================//
    //  Create Laptop                                     //
    //  Post /api/laptops/create                          //
    //====================================================//
    @PostMapping("/create")
    public ResponseEntity<LaptopAddReqDTO> createLaptop(
            @Valid @RequestBody LaptopRequestDTO request) {

        Laptop laptop = laptopService.create(request);

        LaptopAddReqDTO response = LaptopAddReqDTO.builder()
                .code("201")
                .message("Laptop Added Successfully !!")
                .laptopId(laptop.getId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //====================================================//
    //  Update Laptop                                     //
    //  Patch /api/laptops/update                         //
    //====================================================//
    @PatchMapping("/update/{id}")
    public ResponseEntity<LaptopResponseDTO> updateLaptop(
            @PathVariable Long id,
            @RequestBody LaptopRequestDTO request) {

        return ResponseEntity.ok(laptopService.updateLaptop(id, request));
    }



    //====================================================//
    //  Get All Laptops                                   //
    //  Get /api/laptops/getAll                           //
    //====================================================//
    @GetMapping("/getAll")
    public ResponseEntity<List<LaptopResponseDTO>> getAllLaptops() {
        List<LaptopResponseDTO> laptops = laptopService.getAllLaptops();
        return ResponseEntity.ok(laptops);
    }


    //====================================================//
    //  Get Laptop By Id                                  //
    //  Get /api/laptops/getById                          //
    //====================================================//
    @GetMapping("/getById")
    public ResponseEntity<LaptopResponseDTO> getLaptopById(@RequestParam Long laptop_id) {
        return ResponseEntity.ok(laptopService.getById(laptop_id));
    }


    //====================================================//
    //  Delete Laptop By Id(soft/hard)                    //
    //  Delete /api/laptops/delete?laptopId={}&type={}    //
    //====================================================//
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteLaptopById(@RequestParam @Min(1) Long laptopId) {
        String message = laptopService.deleteLaptopById(laptopId);
        return ResponseEntity.ok(Map.of(
                "message", message,
                "status", "success"
        ));
    }


    //====================================================//
    //  Get Laptop By dealerId and Status                 //
    //  Get /api/laptops/getByDealerIdAndStatus           //
    //====================================================//
    @GetMapping("/getByDealerIdAndStatus")
    public ResponseEntity<Page<Laptop>> getLaptopsByDealerIdAndStatus(
            @RequestParam Long sellerId,
            @RequestParam Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy){

        Page<Laptop> laptops = laptopService.getBySellerIdAndStatus(sellerId,status,page,size,sortBy);
        return ResponseEntity.ok((laptops));
    }


    //====================================================//
    //  Get Laptop By status only                         //
    //  Get /api/laptops/getByStatus                      //
    //====================================================//
//    @GetMapping("/getByStatus")
//    public ResponseEntity<Page<Laptop>> getLaptopByStatus(
//            @RequestParam Status status,
//            @RequestParam (defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy) {
//
//        Page<Laptop> laptops = laptopService.getByStatus(status,page,size,sortBy);
//        return  ResponseEntity.ok(laptops);
//    }


    //====================================================//
    //  Get Count of Laptop                               //
    //  Get /api/laptops/getCountByDealerIdAndStatus      //
    //====================================================//
    @GetMapping("/getCountByDealerIdAndStatus")
    public ResponseEntity<Map<String,Object>> countBySellerIdAndStatus(
            @RequestParam Long sellerId,
            @RequestParam Status status){

        Long count = laptopService.countBySellerIdAndStatus(sellerId,status);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Number of laptops for dealer " + sellerId + " with status " + status);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    //====================================================//
//  Get All Laptops By SellerId (Unified API)         //
//  GET /api/laptops/getAllBySellerId                 //
//====================================================//
    @GetMapping("/getAllBySellerId")
    public ResponseEntity<Page<Laptop>> getAllBySellerId(
            @RequestParam Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<Laptop> laptops = laptopService.getAllBySellerId(sellerId, page, size, sortBy);
        return ResponseEntity.ok(laptops);
    }

}