package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Exceptions.InvalidBikeData;
import com.spring.jwt.Bike.Exceptions.StatusNotFoundException;
import com.spring.jwt.Bike.Exceptions.bikeNotFoundException;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.bikeDto;
import com.spring.jwt.repository.SellerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bikeServiceImpl implements bikeService {

    private final bikeRepository bikerepository;
    private final ModelMapper modelMapper;

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
    public bikeDto getBikeById(Long bike_id) {
        Bike bike = bikerepository.findById(bike_id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + bike_id));
        return convertToDto(bike);
    }

    /**
     * UPDATE Bike by ID
     */
    @Override
    public bikeDto updateBike(Long bike_id, bikeDto bikedto) {
        Bike existingBike = bikerepository.findById(bike_id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + bike_id));

        //Make sure the DTO’s ID doesn’t override the entity ID
        bikedto.setBike_id(null);
        // Map updated values from DTO to existing entity
        modelMapper.map(bikedto, existingBike);

        Bike updatedBike = bikerepository.save(existingBike);
        return convertToDto(updatedBike);
    }





    /**
     * SOFT DELETE Bike
     * Instead of deleting the record,  update its status to DELETED
     */
    @Override
    public bikeDto softDeletebike(Long bike_id) {
        Bike bike = bikerepository.findById(bike_id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + bike_id));

        bike.setStatus(bikeStatus.DELETED);

        Bike softDeletedBike = bikerepository.save(bike);
        return convertToDto(softDeletedBike);
    }


    /**
     * HARD DELETE Bike
     * Permanently remove record from the database
     */
    @Override
    public void hardDeleteBike(Long bike_id) {
        if (!bikerepository.existsById(bike_id)) {
            throw new bikeNotFoundException("Bike not found with id: " + bike_id);
        }
        bikerepository.deleteById(bike_id);
    }


    /** Fetch Bikes by Seller & Status (with Pagination) */
    @Override
    public Page<bikeDto> getBikesBySellerAndStatus(Long sellerId, bikeStatus status, int page, int size) {
        Page<Bike> bikes = bikerepository.findBySellerIdAndStatus(sellerId, status, PageRequest.of(page, size));

        if (bikes.isEmpty()) {
            throw new bikeNotFoundException("No bikes found for sellerId: " + sellerId + " with status: " + status);
        }

        return bikes.map(this::convertToDto);
    }


    /** Fetch Bikes by Status (with Pagination) */
    @Override
    public Page<bikeDto> getBikesByStatus(bikeStatus status, int page, int size) {
        Page<Bike> bikesPage = bikerepository.findByStatus(status, PageRequest.of(page, size));

        if (bikesPage.isEmpty()) {
            throw new StatusNotFoundException("No bikes found with status: " + status);
        }

        return bikesPage.map(this::convertToDto);
    }


    /* Count Bikes by Seller & Status */
    @Override
    public Long countBikesBySellerAndStatus(Long sellerId, bikeStatus status) {
        Long count = bikerepository.countBySellerIdAndStatus(sellerId, status);

        if (count == null || count == 0) {
            throw new bikeNotFoundException(
                    "No bikes found for sellerId: " + sellerId + " with status: " + status);
        }

        return count;
    }


}

