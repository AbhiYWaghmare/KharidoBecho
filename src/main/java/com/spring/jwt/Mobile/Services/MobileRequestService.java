package com.spring.jwt.Mobile.Services;

import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.entity.MobileRequest;

import java.util.List;

public interface MobileRequestService {
    MobileRequestResponseDTO createRequest(MobileRequestCreateDTO dto);
    List<MobileRequestResponseDTO> listRequestsForMobile(Long mobileId);
    List<MobileRequestResponseDTO> listRequestsForBuyer(Long buyerId);
    MobileRequestResponseDTO updateRequestStatus(Long requestId, String status);
    MobileRequestResponseDTO appendMessage(Long requestId, Long senderUserId, String message);
    void markRequestCompletedAndMarkSold(Long requestId); // seller confirms final sale
//    List<MobileRequestDTO> getRequestsBySeller(Long sellerId);

}
