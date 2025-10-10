package com.spring.jwt.laptop.controller;

import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.model.Status;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopService;

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
    LaptopResponseDTO laptopResponseDTO = new LaptopResponseDTO();



    //====================================================//
    //  Create Laptop                                     //
    //  Post /api/laptops/create                          //
    //====================================================//
    @PostMapping("/create")
    public ResponseEntity<LaptopResponseDTO> create(@Valid @RequestBody LaptopRequestDTO laptopRequestDTO){
        if (laptopRepository.existsBySerialNumber(laptopRequestDTO.getSerialNumber())) {
            throw new LaptopAlreadyExistsException(
                    "Laptop with serial number " + laptopRequestDTO.getSerialNumber() + " already exists"
            );
        }

        Laptop laptop = laptopService.create(laptopRequestDTO);

       return ResponseEntity.status(HttpStatus.CREATED)
               .body(new LaptopResponseDTO("success","Laptop added successfully with id " +laptop.getId(),"CREATED",200, LocalDateTime.now(),"NULL", laptopResponseDTO.getApiPath(), laptopResponseDTO.getImageUrl()));
    }  

    //====================================================//
    //  Update Laptop                                     //
    //  Patch /api/laptops/update                         //
    //====================================================//
    @PatchMapping("/update")
    public ResponseEntity<LaptopResponseDTO> update(
            @RequestParam Long laptopId,
            @RequestBody LaptopRequestDTO requestDTO) {


            Laptop laptop = laptopService.update(laptopId, requestDTO);
            return ResponseEntity.ok(new LaptopResponseDTO("success","Laptop updated successfully with id " +laptop.getId() ,"UPDATED",200, LocalDateTime.now(),"NULL", laptopResponseDTO.getApiPath(), laptopResponseDTO.getImageUrl()));
    }

    //====================================================//
    //  Get All Laptops                                   //
    //  Get /api/laptops/getAll                           //
    //====================================================//
    @GetMapping("/getAll")
    public ResponseEntity<List<Laptop>> getAllLaptops() {
        List<Laptop> laptops = laptopService.getAllLaptops();
        return ResponseEntity.ok(laptops);
    }

    //====================================================//
    //  Get Laptop By Id                                  //
    //  Get /api/laptops/getById                          //
    //====================================================//
    @GetMapping("/getById")
    public ResponseEntity<Laptop> getLaptopById(@RequestParam Long laptop_id) {
        return ResponseEntity.ok(laptopService.getById(laptop_id));
    }


    //====================================================//
    //  Delete Laptop By Id(soft/hard)                    //
    //  Delete /api/laptops/delete?laptopId={}&type={}    //
    //====================================================//
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteLaptopById(@RequestParam @Min(1) Long laptopId) {
        laptopService.deleteLaptopById(laptopId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Laptop deleted successfully");
        return ResponseEntity.ok(response);
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
    @GetMapping("/getByStatus")
    public ResponseEntity<Page<Laptop>> getLaptopByStatus(
            @RequestParam Status status,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<Laptop> laptops = laptopService.getByStatus(status,page,size,sortBy);
        return  ResponseEntity.ok(laptops);
    }


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
}

