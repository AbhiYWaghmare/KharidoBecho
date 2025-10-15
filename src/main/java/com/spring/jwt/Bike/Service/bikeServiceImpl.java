package com.spring.jwt.Bike.Service;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.FuelType;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.bikeDto;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.Bike.Exceptions.InvalidBikeData;
import com.spring.jwt.Bike.Exceptions.SellerNotFound;
import com.spring.jwt.Bike.Exceptions.StatusNotFoundException;
import com.spring.jwt.Bike.Exceptions.bikeNotFoundException;
import com.spring.jwt.repository.SellerRepository;
import jakarta.transaction.Transactional;
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
        // Convert DTO to Entity
        Bike bike = convertToEntity(bikedto);

        // ===== Validate seller ID =====
        if (bikedto.getSellerId() == null) {
            throw new SellerNotFound("Seller ID is required");
        }

        // Fetch seller from DB
        Seller seller = sellerRepository.findById(bikedto.getSellerId())
                .orElseThrow(() -> new SellerNotFound("Seller not found with ID: " + bikedto.getSellerId()));
        bike.setSeller(seller);

        // ===== Validate status =====
        if (bikedto.getStatus() == null) {
            throw new StatusNotFoundException("Status is required and must be ACTIVE");
        }

        // Only allow ACTIVE status
        if (bikedto.getStatus() != bikeStatus.ACTIVE) {
            throw new StatusNotFoundException("Only ACTIVE status is allowed while Posting  a bike");
        }
        // Accept year till current year

        if (bikedto.getManufactureYear() != null) {
            int currentYear = java.time.Year.now().getValue();

            if (bikedto.getManufactureYear() > currentYear) {
                throw new InvalidBikeData("Manufacture year cannot be in the future (" + currentYear + ")");
            }

            if (bikedto.getManufactureYear() < 2000) {
                throw new InvalidBikeData("Manufacture year must be after 2000.");
            }
        }



        // Save valid bike
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

        // ===== Numeric validations =====
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

        if (bikedto.getKilometersDriven() != null) {
            if (bikedto.getKilometersDriven() < 0) {
                throw new InvalidBikeData("Kilometers driven must be greater than zero ");
            }
            existingBike.setKilometersDriven(bikedto.getKilometersDriven());
        }

        if (bikedto.getManufactureYear() != null) {
            int currentYear = java.time.Year.now().getValue(); // dynamically get current year
            if (bikedto.getManufactureYear() < 1900 || bikedto.getManufactureYear() > currentYear)  {
                throw new InvalidBikeData("Manufacture year must be between 1900 and " + currentYear);
            }
            existingBike.setManufactureYear(bikedto.getManufactureYear());
        }


        // ===== String validations =====
        if (bikedto.getBrand() != null) {
            String brand = bikedto.getBrand().trim();
            if (brand.isBlank()) throw new InvalidBikeData("Brand must not be blank");
            if (brand.length() > 50) throw new InvalidBikeData("Brand must be at most 50 characters");
            if (!brand.matches("^[A-Za-z0-9\\s]+$")) throw new InvalidBikeData("Brand must contain only letters and spaces");
            existingBike.setBrand(brand);
        }

        if (bikedto.getModel() != null) {
            String model = bikedto.getModel().trim();
            if (model.isBlank()) throw new InvalidBikeData("Model must not be blank");
            if (model.length() > 50) throw new InvalidBikeData("Model must be at most 50 characters");
            if (!model.matches("^[A-Za-z0-9\\s-]+$"))
                throw new InvalidBikeData("Model must contain only letters, numbers, spaces, or hyphens");
            existingBike.setModel(model);
        }

        if (bikedto.getVariant() != null) {
            String variant = bikedto.getVariant().trim();
            if (variant.isBlank()) throw new InvalidBikeData("Variant must not be blank");
            if (variant.length() > 50) throw new InvalidBikeData("Variant must be at most 50 characters");
            if (!variant.matches("^[A-Za-z0-9\\s-]+$"))
                throw new InvalidBikeData("Variant must contain only letters, numbers, spaces, or hyphens");
            existingBike.setVariant(variant);
        }

        if (bikedto.getColor() != null) {
            String color = bikedto.getColor().trim();
            if (color.isBlank()) throw new InvalidBikeData("Color must not be blank");
            if (color.length() > 20) throw new InvalidBikeData("Color must be at most 20 characters");
            if (!color.matches("^[A-Za-z\\s]+$")) throw new InvalidBikeData("Color must contain only letters and spaces");
            existingBike.setColor(color);
        }

        if (bikedto.getDescription() != null) {
            String desc = bikedto.getDescription().trim();
            if (desc.isBlank()) throw new InvalidBikeData("Description must not be blank");
            if (desc.length() > 500) throw new InvalidBikeData("Description must be at most 500 characters");
            existingBike.setDescription(desc);
        }

        // ===== Enum validations =====
        if (bikedto.getFuelType() != null) {
            FuelType fuelType = bikedto.getFuelType();
            if (fuelType != FuelType.PETROL && fuelType != FuelType.EV) {
                throw new InvalidBikeData("Fuel type must be PETROL or EV");
            }
            existingBike.setFuelType(fuelType);
        }

        if (bikedto.getStatus() != null) {
            bikeStatus status = bikedto.getStatus();

            // Allowed statuses
            if (status != bikeStatus.NEW &&
                    status != bikeStatus.AVAILABLE &&
                    status != bikeStatus.INACTIVE &&
                    status != bikeStatus.DELETED &&
                    status != bikeStatus.ACTIVE) {

                throw new InvalidBikeData("Status must be one of: NEW, AVAILABLE, INACTIVE, DELETED, ACTIVE");
            }

            existingBike.setStatus(status);
        }


        // ===== Registration number  =====
        if (bikedto.getRegistrationNumber() != null && !bikedto.getRegistrationNumber().isBlank()) {
            String regNo = bikedto.getRegistrationNumber().trim().toUpperCase();

            if (regNo.length() < 6 || regNo.length() > 10) {
                throw new InvalidBikeData("Registration number must be between 6 and 10 characters");
            }

            if (!regNo.matches("^[A-Z]{2}[0-9]{1,2}[A-Z]{1,2}[0-9]{1,4}$")) {
                throw new InvalidBikeData("Invalid registration number format (e.g., MH12AB1234)");
            }

            existingBike.setRegistrationNumber(regNo);
        }


        // ===== Seller update =====
        if (bikedto.getSellerId() != null) {
            Seller seller = sellerRepository.findById(bikedto.getSellerId())
                    .orElseThrow(() -> new SellerNotFound("Seller not found with ID " + bikedto.getSellerId()));
            existingBike.setSeller(seller);
        }

        // ===== Save  =====

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

