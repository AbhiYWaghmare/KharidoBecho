package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Exceptions.*;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.bikeDto;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.repository.SellerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
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
    private final SellerRepository sellerRepository;

    public bikeServiceImpl(bikeRepository bikerepository, ModelMapper modelMapper, SellerRepository sellerRepository) {
        this.bikerepository = bikerepository;
        this.modelMapper = modelMapper;
        this.sellerRepository = sellerRepository;

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
        Bike bike = convertToEntity(bikedto); // Map all other fields

        // Fetch seller from DB
        if (bikedto.getSellerId() == null) {
            throw new SellerNotFound("Seller ID is required");
        }

        Seller seller = sellerRepository.findById(bikedto.getSellerId())
                .orElseThrow(() -> new SellerNotFound("Seller not found with ID: " + bikedto.getSellerId()));

        bike.setSeller(seller);  // set the seller explicitly

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
    @Transactional
    public bikeDto updateBike(Long id, bikeDto bikedto) {
        Bike existingBike = bikerepository.findById(id)
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with id: " + id));

        // Validate and update numeric fields
        if (bikedto.getPrize() != null) {
            if (bikedto.getPrize() <= 0) {
                throw new InvalidBikeData("Prize must be greater than 0");
            }
            existingBike.setPrize(bikedto.getPrize());
        }

        if (bikedto.getEngineCC() != null) {
            if (bikedto.getEngineCC() <= 0) {
                throw new InvalidBikeData("Engine CC must be greater than 0");
            }
            existingBike.setEngineCC(bikedto.getEngineCC());
        }

        // Validate and update String fields
        if (bikedto.getBrand() != null) {
            if (bikedto.getBrand().isBlank()) {
                throw new InvalidBikeData("Brand must not be blank");
            }
            existingBike.setBrand(bikedto.getBrand());
        }

        if (bikedto.getModel() != null) existingBike.setModel(bikedto.getModel());
        if (bikedto.getVariant() != null) existingBike.setVariant(bikedto.getVariant());
        if (bikedto.getManufactureYear() != null) existingBike.setManufactureYear(bikedto.getManufactureYear());
        if (bikedto.getKilometersDriven() != null) existingBike.setKilometersDriven(bikedto.getKilometersDriven());
        if (bikedto.getFuelType() != null) existingBike.setFuelType(bikedto.getFuelType());
        if (bikedto.getColor() != null) existingBike.setColor(bikedto.getColor());
        if (bikedto.getRegistrationNumber() != null) existingBike.setRegistrationNumber(bikedto.getRegistrationNumber());
        if (bikedto.getDescription() != null) existingBike.setDescription(bikedto.getDescription());
        if (bikedto.getStatus() != null) existingBike.setStatus(bikedto.getStatus());

        // Update seller if provided
        if (bikedto.getSellerId() != null) {
            Seller seller = sellerRepository.findById(bikedto.getSellerId())
                    .orElseThrow(() -> new SellerNotFound("Seller not found with ID " + bikedto.getSellerId()));
            existingBike.setSeller(seller);
        }

        Bike savedBike = bikerepository.save(existingBike);
        return convertToDto(savedBike);
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
        Page<Bike> bikes = bikerepository.findBySeller_SellerIdAndStatus(sellerId, status, PageRequest.of(page, size));

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
        Long count = bikerepository.countBySeller_SellerIdAndStatus(sellerId, status);

        if (count == null || count == 0) {
            throw new bikeNotFoundException("No bikes found for sellerId: " + sellerId + " with status: " + status);
        }

        return count;
    }


}

