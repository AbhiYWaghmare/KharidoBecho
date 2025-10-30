//package com.spring.jwt.Mobile.Services;
//
//import com.spring.jwt.Mobile.dto.MobileRequestDTO;
//import com.spring.jwt.Mobile.dto.MobileRequestMessageDTO;
//import com.spring.jwt.Mobile.dto.MobileReqResponseDTO;
//
//import java.util.List;
//
//public interface MobileRequestService {
//    MobileReqResponseDTO createRequest(MobileRequestDTO dto);
//    List<MobileReqResponseDTO> listRequestsForMobile(Long mobileId);
//    List<MobileReqResponseDTO> listRequestsForBuyer(Long buyerId);
//    MobileReqResponseDTO updateRequestStatus(Long requestId, String status);
//    void sendMessage(Long requestId, Long senderId, String message);
//    List<MobileRequestMessageDTO> listMessages(Long requestId);
//    void markMobileSold(Long requestId); // seller confirms sale for accepted request
//}
