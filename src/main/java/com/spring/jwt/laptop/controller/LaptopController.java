package com.spring.jwt.laptop.controller;

import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.entity.Laptop;
import com.spring.jwt.laptop.exceptionHandling.ResourceNotFoundException;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.service.LaptopService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laptops")
@RequiredArgsConstructor
public class LaptopController {

   private LaptopRepository laptopRepository;
   private LaptopService laptopService;



    //To Create new laptop entry
    @PostMapping("/create")
    public ResponseEntity<Laptop> createLaptop(@Valid @RequestBody LaptopRequestDTO requestDTO){
        Laptop savedLaptop = laptopService.create(requestDTO);
        return ResponseEntity.status(201).body(savedLaptop);
    }

    //Updating laptop
    @PatchMapping("/update/{laptopId}")
    public ResponseEntity<Laptop> updateLaptop(
            @PathVariable Long laptopId,
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) String dealer,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price) {

        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop with ID " + laptopId + " not found"));

        if(serialNumber!=null){
            if(serialNumber.isBlank()){
                throw new IllegalArgumentException("Serial number can not be blank!");
            }
            laptop.setSerialNumber(serialNumber);
        }

        if(dealer!=null){
            if (dealer.isBlank()){
                throw  new IllegalArgumentException("Dealer can not be blank!");
            }
            laptop.setDealer(dealer);
        }

        if (model!=null){
            if(model.isBlank()){
                throw new IllegalArgumentException("Model can not be blank!");
            }
            laptop.setModel(model);
        }

        if(price!=null){
            if(price<=0){
                throw new IllegalArgumentException("Price must be positive!");
            }
            laptop.setPrice(price);
        }

        Laptop updatedLaptop = laptopService.update(laptopId,serialNumber,dealer, model, brand,price);
        return ResponseEntity.ok(updatedLaptop);
    }



    //To Get All laptops
    @GetMapping("/getAll")
    public ResponseEntity<List<Laptop>> getAllLaptops() {
        List<Laptop> laptops = laptopService.getAllLaptops();
        return ResponseEntity.ok(laptops);
    }

    //Get Laptops by ID
    @GetMapping("/getById")
    public ResponseEntity<Laptop> getLaptopById(@RequestParam Long id) {
       return ResponseEntity.ok(laptopService.getById(id));
    }

    @DeleteMapping("/delete/{laptopId}")
    public String deleteLaptop(Long laptopId, String type){
        laptopService.deleteById(laptopId,type);

        return "Laptop " +laptopId + " deleted ("+type+")";
    }


}
