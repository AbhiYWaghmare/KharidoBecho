package com.spring.jwt.laptop.service;

import com.spring.jwt.laptop.dto.LaptopRequestCreateDTO;
import com.spring.jwt.laptop.dto.LaptopRequestDTO;
import com.spring.jwt.laptop.dto.LaptopRequestResponseDTO;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
import com.spring.jwt.laptop.entity.LaptopBooking;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@Service
public interface LaptopRequestService {
    LaptopRequestResponseDTO createRequest(LaptopRequestCreateDTO dto);
    List<LaptopRequestResponseDTO> listRequestsForLaptop(Long laptopId);
    List<LaptopRequestResponseDTO> listRequestsForBuyer(Long buyerId);
    LaptopRequestResponseDTO updateRequestStatus(Long requestId, String status);
    LaptopRequestResponseDTO appendMessage(Long requestId, Long senderUserId, String message);
    void markRequestCompletedAndMarkSold(Long requestId);

    LaptopRequestResponseDTO getRequestById(Long requestId);

    boolean userExists(Long senderId);

    List<LaptopRequestResponseDTO> listRequestsForSeller(Long sellerId);

}
