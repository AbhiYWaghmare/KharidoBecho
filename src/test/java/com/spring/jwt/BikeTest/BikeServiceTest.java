package com.spring.jwt.BikeTest;

import com.spring.jwt.Bike.Entity.Bike;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.Service.bikeServiceImpl;
import com.spring.jwt.Bike.dto.bikeDto;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BikeServiceTest {

    @Mock
    private bikeRepository bikerepository;

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private bikeServiceImpl bikeService;

    private bikeDto bikedto;
    private Bike bike1;

    @BeforeEach
    public void setUp() {
        bikeService = new bikeServiceImpl(bikerepository,  new ModelMapper(),sellerRepository);

        bikedto = new bikeDto();
        bikedto.setSellerId(1L);
        bikedto.setStatus(bikeStatus.ACTIVE);
        bikedto.setManufactureYear(2020);

        bike1 = new Bike();
        bike1.setBike_id(1L);
        bike1.setBrand("Yamaha");
    }

    @Test
    public void testCreateBike_Success() {
        Seller seller = new Seller();
        seller.setSellerId(1L);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(bikerepository.save(any(Bike.class))).thenReturn(bike1);

        bikeDto result = bikeService.createBike(bikedto);

        assertNotNull(result);
        verify(bikerepository, times(1)).save(any(Bike.class));
    }

    @Test
    void testGetAllBikes_ShouldReturnListOfBikeDtos() {
        when(bikerepository.findAll()).thenReturn(Arrays.asList(bike1,bike1));

        List<bikeDto> result = bikeService.getAllBikes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bikerepository, times(1)).findAll();
    }

    @Test
    void getBikeById_ShouldReturnBikeDto_WhenBikeExists() {
        when(bikerepository.findById(1L)).thenReturn(Optional.of(bike1));

        bikeDto result = bikeService.getBikeById(1L);

        assertNotNull(result);
        assertEquals("Yamaha", result.getBrand());
        verify(bikerepository, times(1)).findById(1L);
    }
    @Test
    void testSoftDeleteBike_ShouldMarkBikeAsDeleted() {
        // Arrange
        Long bikeId = 1L;

        Bike bike = new Bike();
        bike.setBike_id(bikeId);
        bike.setStatus(bikeStatus.ACTIVE);

        // Mock repository
        when(bikerepository.findById(bikeId)).thenReturn(Optional.of(bike));
        when(bikerepository.save(any(Bike.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        bikeDto result = bikeService.softDeletebike(bikeId);

        // Assert
        assertNotNull(result);
        assertEquals(bikeStatus.DELETED, result.getStatus());
        verify(bikerepository, times(1)).findById(bikeId);
        verify(bikerepository, times(1)).save(any(Bike.class));
    }

    @Test
    void testHardDeleteBike_ShouldDeleteWhenExists() {
        // Arrange
        Long bikeId = 1L;
        when(bikerepository.existsById(bikeId)).thenReturn(true);

        // Act
        bikeService.hardDeleteBike(bikeId);

        // Assert
        verify(bikerepository, times(1)).existsById(bikeId);
        verify(bikerepository, times(1)).deleteById(bikeId);
    }


    @Test
    void testGetBikesBySellerAndStatus_Success() {
        Long sellerId = 1L;
        bikeStatus status = bikeStatus.ACTIVE;

        Page<Bike> mockPage = new PageImpl<>(List.of(bike1));

        when(bikerepository.findBySeller_SellerIdAndStatus(eq(sellerId), eq(status), any(PageRequest.class)))
                .thenReturn(mockPage);


        Page<bikeDto> result = bikeService.getBikesBySellerAndStatus(sellerId, status, 0, 5);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Yamaha", result.getContent().get(0).getBrand());

        verify(bikerepository, times(1))
                .findBySeller_SellerIdAndStatus(eq(sellerId), eq(status), any(PageRequest.class));
    }

    @Test
    void testCountBikesBySellerAndStatus_Success() {
        // Arrange
        Long sellerId = 1L;
        bikeStatus status = bikeStatus.ACTIVE;

        // Mock repository to return count = 5
        when(bikerepository.countBySeller_SellerIdAndStatus(sellerId, status))
                .thenReturn(5L);

        // Act
        Long result = bikeService.countBikesBySellerAndStatus(sellerId, status);

        // Assert
        assertNotNull(result);
        assertEquals(5L, result);  // should return the same count
        verify(bikerepository, times(1))
                .countBySeller_SellerIdAndStatus(sellerId, status);
    }




}
