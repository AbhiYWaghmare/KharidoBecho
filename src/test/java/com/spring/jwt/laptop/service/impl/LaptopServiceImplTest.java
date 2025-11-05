//package com.spring.jwt.laptop.service.impl;
//
//import com.spring.jwt.entity.Seller;
//import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
//import com.spring.jwt.exception.laptop.LaptopNotFoundException;
//import com.spring.jwt.exception.mobile.SellerNotFoundException;
//import com.spring.jwt.laptop.dto.LaptopRequestDTO;
//import com.spring.jwt.laptop.entity.Laptop;
//import com.spring.jwt.laptop.repository.LaptopRepository;
//import com.spring.jwt.repository.SellerRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Pageable;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LaptopServiceImplTest {
//
//    @Mock
//    private LaptopRepository laptopRepository;
//
//    @Mock
//    private SellerRepository sellerRepository;
//
//    @InjectMocks
//    private LaptopServiceImpl laptopService;
//
//    private Laptop laptop;
//    private Seller seller;
//    private LaptopRequestDTO laptopRequestDTO;
//
//    @BeforeEach
//    void setUp() {
//
//        seller = new Seller();
//        seller.setSellerId(1L);
//
//        laptop = new Laptop();
//        laptop.setId(1L);
//        laptop.setSerialNumber("SERIAL_TEST_001");
//        laptop.setStatus(Status.AVAILABLE);
//        laptop.setDeleted(false);
//        laptop.setSeller(seller);
//        laptop.setPrice(1000.0);
//
//        laptopRequestDTO = new LaptopRequestDTO();
//        laptopRequestDTO.setSerialNumber("SERIAL_TEST_001");
//        laptopRequestDTO.setSellerId(1L);
//        laptopRequestDTO.setPrice(1000.0);
//        laptopRequestDTO.setBrand("BrandX");
//    }
//
//    // ----------------- CREATE -----------------
//    @Test
//    void create_ShouldSaveLaptop_WhenSellerExists() {
//        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
////        when(laptopRepository.existsBySerialNumber(anyString())).thenReturn(false);
//        when(laptopRepository.save(any(Laptop.class))).thenReturn(laptop);
//
//        Laptop result = laptopService.create(laptopRequestDTO);
//
//        assertNotNull(result);
//        assertEquals("SERIAL_TEST_001", result.getSerialNumber());
//        verify(laptopRepository, times(1)).save(any(Laptop.class));
//    }
//
//    @Test
//    void create_ShouldThrowException_WhenSellerNotFound() {
//        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());
//        laptopRequestDTO.setSellerId(999L);
//
//        assertThrows(SellerNotFoundException.class, () -> laptopService.create(laptopRequestDTO));
//    }
//
//    @Test
//    void create_ShouldThrowException_WhenLaptopAlreadyExists() {
//
//        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
//        when(laptopRepository.existsBySerialNumber("SERIAL_TEST_001")).thenReturn(true);
//
//        laptopRequestDTO.setSellerId(1L);
//        laptopRequestDTO.setSerialNumber("SERIAL_TEST_001");
//
//        assertThrows(LaptopAlreadyExistsException.class, () -> laptopService.create(laptopRequestDTO));
//
//        verify(laptopRepository, never()).save(any(Laptop.class));
//    }
//
//    // ----------------- GET BY ID -----------------
//    @Test
//    void getById_ShouldReturnLaptop_WhenExists() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.of(laptop));
//
//        Laptop result = laptopService.getById(1L);
//
//        assertEquals(1L, result.getId());
//    }
//
//    @Test
//    void getById_ShouldThrowException_WhenNotFound() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.getById(2L));
//    }
//
//    // ----------------- UPDATE -----------------
//    @Test
//    void update_ShouldModifyLaptop_WhenFound() {
//        Long laptopId = 1L;
//
//        Laptop existingLaptop = new Laptop();
//        existingLaptop.setId(laptopId);
//        existingLaptop.setSerialNumber("SERIAL_TEST_001");
//        existingLaptop.setSeller(seller);
//
//        when(laptopRepository.findById(laptopId)).thenReturn(Optional.of(existingLaptop));
//        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
//        when(laptopRepository.save(any(Laptop.class))).thenReturn(existingLaptop);
//
//        laptopRequestDTO.setSellerId(1L);
//        laptopRequestDTO.setSerialNumber("SERIAL_TEST_002");
//
//        Laptop updatedLaptop = laptopService.update(laptopId, laptopRequestDTO);
//
//        assertNotNull(updatedLaptop);
//        assertEquals("SERIAL_TEST_002", updatedLaptop.getSerialNumber());
//        verify(laptopRepository, times(1)).save(any(Laptop.class));
//    }
//
//    @Test
//    void update_ShouldThrowException_WhenLaptopNotFound() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.update(2L, laptopRequestDTO));
//    }
//
//    @Test
//    void update_ShouldThrowException_WhenDuplicateSerial() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.of(laptop));
//        when(laptopRepository.existsBySerialNumber("DUPLICATE_SN")).thenReturn(true);
//
//        laptopRequestDTO.setSerialNumber("DUPLICATE_SN");
//
//        assertThrows(LaptopAlreadyExistsException.class, () -> laptopService.update(1L, laptopRequestDTO));
//    }
//
//    // ----------------- DELETE -----------------
//    @Test
//    void deleteLaptopById_ShouldSoftDelete_WhenActive() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.of(laptop));
//        when(laptopRepository.save(any(Laptop.class))).thenReturn(laptop);
//
//        laptopService.deleteLaptopById(1L);
//
//        assertTrue(laptop.isDeleted());
//        verify(laptopRepository, times(1)).save(laptop);
//    }
//
//    @Test
//    void deleteLaptopById_ShouldThrow_WhenNotFound() {
//        when(laptopRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.deleteLaptopById(2L));
//    }
//
//    // ----------------- GET ALL -----------------
//    @Test
//    void getAllLaptops_ShouldReturnList() {
//        when(laptopRepository.findAll()).thenReturn(List.of(laptop));
//
//        List<Laptop> result = laptopService.getAllLaptops();
//
//        assertEquals(1, result.size());
//    }
//
//    // ----------------- GET BY SELLER & STATUS -----------------
//    @Test
//    void getByDealerIdAndStatus_ShouldReturnPage() {
//        Seller seller = new Seller();
//        seller.setSellerId(1L);
//        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
//
//        // Mock the laptops page
//        Page<Laptop> page = new PageImpl<>(List.of(laptop));
//        when(laptopRepository.findBySellerIdAndStatus(
//                eq(1L),
//                eq(Status.AVAILABLE),
//                any(Pageable.class)
//        )).thenReturn(page);
//
//        // Act
//        Page<Laptop> result = laptopService.getBySellerIdAndStatus(1L, Status.AVAILABLE, 0, 10, "id");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.getTotalElements());
//
//        // Verify repository calls
//        verify(sellerRepository, times(1)).findById(1L);
//        verify(laptopRepository, times(1))
//                .findBySellerIdAndStatus(eq(1L), eq(Status.AVAILABLE), any(Pageable.class));
//    }
//
//    @Test
//    void getByStatus_ShouldReturnPage() {
//        Page<Laptop> page = new PageImpl<>(List.of(laptop));
//        when(laptopRepository.findByStatus(any(Status.class), any(PageRequest.class))).thenReturn(page);
//
//        Page<Laptop> result = laptopService.getByStatus(Status.AVAILABLE, 0, 10, "id");
//
//        assertEquals(1, result.getTotalElements());
//    }
//
//    // ----------------- COUNT -----------------
//    @Test
//    void countBySellerIdAndStatus_ShouldReturnCount() {
//        when(laptopRepository.countBySellerAndStatus(anyLong(), any(Status.class))).thenReturn(1L);
//
//        Long count = laptopService.countBySellerIdAndStatus(1L, Status.AVAILABLE);
//
//        assertEquals(1L, count);
//    }
////}