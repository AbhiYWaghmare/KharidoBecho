//package com.spring.jwt.laptop.service.impl;
//
//import com.spring.jwt.Mobile.entity.RequestStatus;
//import com.spring.jwt.entity.User;
//import com.spring.jwt.exception.laptopChat.LaptopChatNotAllowedException;
//import com.spring.jwt.exception.laptopChat.LaptopChatRequestNotFoundException;
//import com.spring.jwt.exception.laptopChat.LaptopChatSenderNotFoundException;
//import com.spring.jwt.exception.laptopChat.LaptopChatUnauthorizedSenderException;
//import com.spring.jwt.laptop.dto.LaptopChatMessageDTO;
//import com.spring.jwt.laptop.dto.LaptopChatSendDTO;
//import com.spring.jwt.laptop.entity.LaptopBooking;
//import com.spring.jwt.laptop.entity.LaptopChatMessages;
//import com.spring.jwt.laptop.mapper.LaptopChatMapper;
//import com.spring.jwt.laptop.model.LaptopRequestStatus;
//import com.spring.jwt.laptop.repository.LaptopChatMessageRepository;
//import com.spring.jwt.laptop.repository.LaptopRequestRepository;
//import com.spring.jwt.laptop.service.LaptopChatService;
//import com.spring.jwt.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class LaptopChatServiceImpl implements LaptopChatService {
//    private final LaptopRequestRepository requestRepo;
//    private final UserRepository userRepo;
//    private final LaptopChatMessageRepository chatRepo;
//
//    // ================= SEND MESSAGE =================
//
//    @Override
//    public LaptopChatMessageDTO sendMessage(LaptopChatSendDTO dto) {
//
//        LaptopBooking request = requestRepo.findById(dto.getRequestId())
//                .orElseThrow(() ->
//                        new LaptopChatRequestNotFoundException(dto.getRequestId()));
//
//        if (request.getPendingStatus() != LaptopRequestStatus.IN_NEGOTIATION) {
//            throw new LaptopChatNotAllowedException(
//                    "Chat allowed only when request is IN_NEGOTIATION"
//            );
//        }
//
//        User sender = userRepo.findById(dto.getSenderUserId())
//                .orElseThrow(() ->
//                        new LaptopChatSenderNotFoundException(dto.getSenderUserId()));
//
//        validateSender(request, sender);
//
//        LaptopChatMessages message = LaptopChatMessages.builder()
//                .request(request)
//                .sender(sender)
//                .message(dto.getMessage())
//                .build();
//
//        chatRepo.save(message);
//
//        boolean isBuyer =
//                sender.getId().equals(request.getBuyer().getUser().getId());
//
//        Long receiverUserId = isBuyer
//                ? request.getSeller().getUser().getId()
//                : request.getBuyer().getUser().getId();
//
//        String senderRole = isBuyer ? "BUYER" : "SELLER";
//
//        return LaptopChatMapper.toDTO(
//                message,
//                senderRole,
//                receiverUserId
//        );
//    }
//
//    // ================= GET CHAT =================
//
//    @Override
//    @Transactional(Transactional.TxType.SUPPORTS)
//    public List<LaptopChatMessageDTO> getChat(Long requestId) {
//
//        LaptopBooking request = requestRepo.findById(requestId)
//                .orElseThrow(() ->
//                        new LaptopChatRequestNotFoundException(requestId));
//
//        return chatRepo
//                .findByRequest_LaptopBookingId(requestId)
//                .stream()
//                .map(msg -> {
//
//                    boolean isBuyer =
//                            msg.getSender().getId()
//                                    .equals(request.getBuyer().getUser().getId());
//
//                    String role = isBuyer ? "BUYER" : "SELLER";
//
//                    Long receiverId = isBuyer
//                            ? request.getSeller().getUser().getId()
//                            : request.getBuyer().getUser().getId();
//
//                    return LaptopChatMapper.toDTO(msg, role, receiverId);
//                })
//                .toList();
//    }
//
//    // ================= VALIDATION =================
//
//    @Override
//    public void validateSender(LaptopBooking req, User sender) {
//
//        if (req.getBuyer() == null || req.getSeller() == null) {
//            throw new LaptopChatUnauthorizedSenderException(
//                    "Invalid request participants");
//        }
//
//        Long senderId = sender.getId();
//        Long buyerUserId = req.getBuyer().getUser().getId();
//        Long sellerUserId = req.getSeller().getUser().getId();
//
//        if (!senderId.equals(buyerUserId) &&
//                !senderId.equals(sellerUserId)) {
//
//            throw new LaptopChatUnauthorizedSenderException(
//                    "User is not part of this request"
//            );
//        }
//    }
//}
