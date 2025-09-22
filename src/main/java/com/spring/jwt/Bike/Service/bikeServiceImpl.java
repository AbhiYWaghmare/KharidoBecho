package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Exceptions.bikeNotFoundException;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.bikeDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bikeServiceImpl implements bikeService {

    private final bikeRepository bikerepository;
    private final ModelMapper modelMapper; // Corrected

    public bikeServiceImpl(bikeRepository bikerepository, ModelMapper modelMapper) {
        this.bikerepository = bikerepository;
        this.modelMapper = modelMapper;
    }

    private Bike convertToEntity(bikeDto bikedto) {
        return modelMapper.map(bikedto, Bike.class);
    }

    private bikeDto convertToDto(Bike bike) {
        return modelMapper.map(bike, bikeDto.class);
    }

    /**
     * CREATE Bike
     */
    @Override
    public bikeDto createBike(bikeDto bikedto) {
        Bike bike = convertToEntity(bikedto);
        Bike savedBike = bikerepository.save(bike);
        return convertToDto(savedBike);
    }

    /**
     * GET Bike by passing a DTO
     */
    @Override
    public List<bikeDto> getAllBikes() {
        return bikerepository.findAll() // Fetch all bikes from DB
                .stream()
                .map(this::convertToDto) // Convert each Bike entity to bikeDto
                .toList();
    }


    /**
     * GET Bike by ID
     */
    @Override
    public bikeDto getBikeById(Long id) {
        Bike bike = bikerepository.findById(id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + id));
        return convertToDto(bike);
    }

    /**
     * UPDATE Bike by ID
     */
    @Override
    public bikeDto updateBike(Long id, bikeDto bikedto) {
        Bike existingBike = bikerepository.findById(id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + id));

        // Map updated values from DTO to existing entity
        modelMapper.map(bikedto, existingBike);

        Bike updatedBike = bikerepository.save(existingBike);
        return convertToDto(updatedBike);
    }

    /**
     * SOFT DELETE Bike
     * Instead of deleting the record, we update its status to DELETED
     */
    @Override
    public bikeDto softDeletebike(Long id) {
        Bike bike = bikerepository.findById(id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + id));

        bike.setStatus(bikeStatus.DELETED);

        Bike softDeletedBike = bikerepository.save(bike);
        return convertToDto(softDeletedBike);
    }


    /**
     * HARD DELETE Bike
     * Permanently remove record from the database
     */
    @Override
    public void hardDeleteBike(Long id) {
        if (!bikerepository.existsById(id)) {
            throw new bikeNotFoundException("Bike not found with id: " + id);
        }
        bikerepository.deleteById(id);
    }


    /** Fetch Bikes by Seller & Status (with Pagination) */
    @Override
    public Page<bikeDto> getBikesBySellerAndStatus(Long sellerId, bikeStatus status, int page, int size) {
        return bikerepository.findBySellerIdAndStatus(sellerId, status, PageRequest.of(page, size))
                .map(this::convertToDto);
    }

    /** Fetch Bikes by Status (with Pagination) */
    @Override
    public Page<bikeDto> getBikesByStatus(bikeStatus status, int page, int size) {
        return bikerepository.findByStatus(status, PageRequest.of(page, size))
                .map(this::convertToDto);
    }

    /* Count Bikes by Seller & Status */
    @Override
    public Long countBikesBySellerAndStatus(Long sellerId, bikeStatus status) {
        return bikerepository.countBySellerIdAndStatus(sellerId, status);
    }

}

