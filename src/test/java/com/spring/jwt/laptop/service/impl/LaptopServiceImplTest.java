//package com.spring.jwt.laptop.service.impl;
//
//import com.spring.jwt.entity.Seller;
//import com.spring.jwt.exception.laptop.LaptopAlreadyExistsException;
//import com.spring.jwt.exception.laptop.LaptopNotFoundException;
//import com.spring.jwt.exception.mobile.SellerNotFoundException;
//import com.spring.jwt.laptop.dto.LaptopRequestDTO;
//import com.spring.jwt.laptop.entity.Laptop;
//import com.spring.jwt.laptop.model.Status;
//import com.spring.jwt.laptop.repository.LaptopRepository;
//import com.spring.jwt.repository.SellerRepository;
////import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
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
//    @InjectMocks
//    private LaptopServiceImpl laptopService;
//
//    private Laptop laptop;
//    private Seller seller;
//    private LaptopRequestDTO laptopRequestDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Seller
//        seller = new Seller();
//        seller.setSellerId(1L);
//
//        // Laptop
//        laptop = new Laptop();
//        laptop.setId(1L);
//        laptop.setSerialNumber("SERIAL_TEST_001");
//        laptop.setStatus(Status.AVAILABLE);
//        laptop.setDeleted(false);
//        laptop.setSeller(seller);
//
//        // DTO
//        laptopRequestDTO = new LaptopRequestDTO();
//        laptopRequestDTO.setSerialNumber("SERIAL_TEST_001");
//        laptopRequestDTO.setSellerId(1L);
//        laptopRequestDTO.setPrice(1000.0);
//        laptopRequestDTO.setBrand("BrandX");
//
//        // Common stubs
//        when(laptopRepository.findById(1L)).thenReturn(Optional.of(laptop));
//        when(laptopRepository.existsBySerialNumber("SERIAL_TEST_001")).thenReturn(false);
//        when(laptopRepository.countBySellerAndStatus(1L, Status.AVAILABLE)).thenReturn(1L);
//
//        Page<Laptop> page = new PageImpl<>(List.of(laptop));
//        when(laptopRepository.findBySellerAndStatus(1L, Status.AVAILABLE, Pageable.unpaged()))
//                .thenReturn(page);
//        when(laptopRepository.findByStatus(Status.AVAILABLE, Pageable.unpaged()))
//                .thenReturn(page);
//    }
//
//    // ----------------- CREATE -----------------
//    @Test
//    void create_ShouldSaveLaptop_WhenSellerExists() {
//        laptopService.create(laptopRequestDTO);
//        verify(laptopRepository, times(1)).save(any(Laptop.class));
//    }
//
//    @Test
//    void create_ShouldThrowException_WhenSellerNotFound() {
//        laptopRequestDTO.setSellerId(999L); // non-existent
//        Exception exception = assertThrows(Exception.class, () -> {
//            laptopService.create(laptopRequestDTO);
//        });
//        assertTrue(exception instanceof RuntimeException || exception instanceof LaptopNotFoundException);
//    }
//
//    // ----------------- GET BY ID -----------------
//    @Test
//    void getById_ShouldReturnLaptop_WhenExists() {
//        Laptop result = laptopService.getById(1L);
//        assertEquals(laptop.getId(), result.getId());
//    }
//
//    @Test
//    void getById_ShouldThrowException_WhenNotFound() {
//        when(laptopRepository.findById(2L)).thenReturn(Optional.empty());
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.getById(2L));
//    }
//
//    // ----------------- UPDATE -----------------
//    @Test
//    void update_ShouldModifyLaptop_WhenFound() {
//        laptopRequestDTO.setPrice(2000.0);
//        Laptop updated = laptopService.update(1L, laptopRequestDTO);
//        assertEquals(2000.0, updated.getPrice());
//    }
//
//    @Test
//    void update_ShouldThrowException_WhenLaptopNotFound() {
//        when(laptopRepository.findById(2L)).thenReturn(Optional.empty());
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.update(2L, laptopRequestDTO));
//    }
//
//    @Test
//    void update_ShouldThrowException_WhenDuplicateSerial() {
//        when(laptopRepository.existsBySerialNumber("SERIAL_DUPLICATE")).thenReturn(true);
//        laptopRequestDTO.setSerialNumber("SERIAL_DUPLICATE");
//        assertThrows(LaptopAlreadyExistsException.class, () -> laptopService.update(1L, laptopRequestDTO));
//    }
//
//    // ----------------- DELETE -----------------
//    @Test
//    void deleteLaptopById_ShouldSoftDelete_WhenActive() {
//        laptopService.deleteLaptopById(1L);
//        assertTrue(laptop.isDeleted());
//    }
//
//    @Test
//    void deleteLaptopById_ShouldThrow_WhenNotFound() {
//        when(laptopRepository.findById(2L)).thenReturn(Optional.empty());
//        assertThrows(LaptopNotFoundException.class, () -> laptopService.deleteLaptopById(2L));
//    }
//
//    // ----------------- GET ALL -----------------
//    @Test
//    void getAllLaptops_ShouldReturnList() {
//        List<Laptop> result = laptopService.getAllLaptops();
//        assertEquals(1, result.size());
//    }
//
//    // ----------------- GET BY STATUS / SELLER -----------------
//    @Test
//    void getBySellerIdAndStatus_ShouldReturnPage() {
//        Page<Laptop> page = laptopService.getByDealerIdAndStatus(1L, Status.AVAILABLE, Pageable.unpaged());
//        assertEquals(1, page.getTotalElements());
//    }
//
//    @Test
//    void getByStatus_ShouldReturnPage() {
//        Page<Laptop> page = laptopService.getByStatus(Status.AVAILABLE, Pageable.unpaged());
//        assertEquals(1, page.getTotalElements());
//    }
//
//    // ----------------- COUNT -----------------
//    @Test
//    void countBySellerIdAndStatus_ShouldReturnCount() {
//        Long count = laptopService.countBySellerIdAndStatus(1L, Status.AVAILABLE);
//        assertEquals(1L, count);
//    }
//}