package com.spring.jwt.Mobile.Services;

import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.entity.MobileRequest;

import java.util.List;

public interface MobileRequestService {
    MobileRequestResponseDTO createRequest(MobileRequestCreateDTO dto);
    List<MobileRequestResponseDTO> listRequestsForMobile(Long mobileId);
    List<MobileRequestResponseDTO> listRequestsForBuyer(Long buyerId);
//    MobileRequestResponseDTO updateRequestStatus(Long requestId, String status);
    MobileRequestResponseDTO acceptRequest(Long requestId);
    MobileRequestResponseDTO rejectRequest(Long requestId);
    void completeRequest(Long requestId);
    MobileRequestResponseDTO appendMessage(Long requestId, Long senderUserId, String message);
//    void markRequestCompletedAndMarkSold(Long requestId); // seller confirms final sale
//    List<MobileRequestDTO> getRequestsBySeller(Long sellerId);
    List<MobileRequestResponseDTO> listRequestsForSeller(Long sellerId);


}
