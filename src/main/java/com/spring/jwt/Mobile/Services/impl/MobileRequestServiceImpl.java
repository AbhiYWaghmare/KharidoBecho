package com.spring.jwt.Mobile.Services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.jwt.Mobile.Mapper.MobileMapper;
import com.spring.jwt.Mobile.entity.ConversationMessage;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.User;
import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.entity.MobileRequest;
import com.spring.jwt.Mobile.entity.RequestStatus;
import com.spring.jwt.exception.mobile.MobileRequestException;
import com.spring.jwt.exception.mobile.MobileRequestNotFoundException;
import com.spring.jwt.Mobile.Repository.MobileRequestRepository;
import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.Mobile.Repository.MobileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileRequestServiceImpl implements MobileRequestService {

    private final MobileRequestRepository requestRepo;
    private final MobileRepository mobileRepo;
    private final BuyerRepository buyerRepo;
    private final SellerRepository sellerRepo;
    private final UserRepository userRepo;
//    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // enables OffsetDateTime, LocalDateTime, etc.
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @Override
    @Transactional
    public MobileRequestResponseDTO createRequest(MobileRequestCreateDTO dto) {
        var mobile = mobileRepo.findByMobileIdAndDeletedFalse(dto.getMobileId())
                .orElseThrow(() -> new MobileRequestException("Mobile not found: " + dto.getMobileId()));

        if (mobile.getStatus() != null && mobile.getStatus().name().equalsIgnoreCase("SOLD")) {
            throw new MobileRequestException("Product already sold.");
        }

        User buyerUser = userRepo.findById(dto.getBuyerUserId())
                .orElseThrow(() -> new MobileRequestException("Buyer (user) not found: " + dto.getBuyerUserId()));

        // verify buyer has a Buyer record
        Buyer buyer = buyerRepo.findByUser_IdAndDeletedFalse(dto.getBuyerUserId())
                .orElseThrow(() -> new MobileRequestException("User is not registered as a buyer: " + dto.getBuyerUserId()));

        Seller seller = mobile.getSeller();
        if (seller == null) throw new MobileRequestException("Mobile has no seller assigned");

        // prevent duplicate request from same buyer for same mobile
        var existing = requestRepo.findByMobile_MobileIdAndStatus(mobile.getMobileId(), RequestStatus.PENDING);
        boolean alreadyRequested = requestRepo.findByBuyer_BuyerId(buyer.getBuyerId())
                .stream().anyMatch(r -> r.getMobile().getMobileId().equals(mobile.getMobileId()));
        if (alreadyRequested) {
            throw new MobileRequestException("You have already sent a request for this product.");
        }

        MobileRequest r = MobileRequest.builder()
                .mobile(mobile)
                .buyer(buyer)
                .seller(seller)
                .status(RequestStatus.PENDING)
                .conversation("[]")
                .build();

        // append initial message if provided
        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            appendMessageInternal(r, buyerUser.getId(), "BUYER", dto.getMessage());
        }

        try {
            r = requestRepo.save(r);
        } catch (DataIntegrityViolationException e) {
            // if you use DB unique constraints, handle them here
            throw new MobileRequestException("Failed to create request: " + e.getMessage(), e);
        }

        return toResponse(r);
    }

    @Override
    public List<MobileRequestResponseDTO> listRequestsForMobile(Long mobileId) {
        return requestRepo.findByMobile_MobileIdOrderByCreatedAtAsc(mobileId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    public List<MobileRequestResponseDTO> listRequestsForBuyer(Long buyerId) {
        return requestRepo.findByBuyer_BuyerId(buyerId).stream()
                .map(this::toResponse).toList();
    }

//    @Override
//    public List<MobileRequestDTO> getRequestsBySeller(Long sellerId) {
//        List<MobileRequest> requests = MobileRequestRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
//
//        return requests.stream()
//                .map(MobileMapper::toDTO)
//                .collect(Collectors.toList());
//    }


    @Override
    @Transactional
    public MobileRequestResponseDTO updateRequestStatus(Long requestId, String statusStr) {
        MobileRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        RequestStatus newStatus;
        try {
            newStatus = RequestStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new MobileRequestException("Invalid status: " + statusStr);
        }

        //  In production multi-instance use DB constraint or distributed lock.
        synchronized (("mobile-lock-" + req.getMobile().getMobileId()).intern()) {
            if (newStatus == RequestStatus.ACCEPTED) {
                // ensure no other accepted request exists
                boolean exists = requestRepo.existsByMobile_MobileIdAndStatus(req.getMobile().getMobileId(), RequestStatus.ACCEPTED);
                if (exists) throw new MobileRequestException("Another request already ACCEPTED for this mobile.");
                req.setStatus(RequestStatus.IN_NEGOTIATION); // move to negotiation first
                // optionally set mobile status to reserved/active
                req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.ACTIVE);
                mobileRepo.save(req.getMobile());
            } else if (newStatus == RequestStatus.REJECTED) {
                req.setStatus(RequestStatus.REJECTED);
                // if no accepted requests exist, you can revert mobile to AVAILABLE (if not sold)
                boolean anyAccepted = requestRepo.existsByMobile_MobileIdAndStatus(req.getMobile().getMobileId(), RequestStatus.ACCEPTED);
                if (!anyAccepted && req.getMobile().getStatus() != com.spring.jwt.Mobile.entity.Mobile.Status.SOLD) {
                    req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.ACTIVE); // or AVAILABLE depending on your Mobile.Status enum
                    mobileRepo.save(req.getMobile());
                }
            } else if (newStatus == RequestStatus.IN_NEGOTIATION) {
                req.setStatus(RequestStatus.IN_NEGOTIATION);
                req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.ACTIVE);
                mobileRepo.save(req.getMobile());
            } else if (newStatus == RequestStatus.COMPLETED) {
                // completed -> final sold
                req.setStatus(RequestStatus.COMPLETED);
                req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.SOLD);
                mobileRepo.save(req.getMobile());

                // reject other pending requests
                var others = requestRepo.findByMobile_MobileIdAndStatus(req.getMobile().getMobileId(), RequestStatus.PENDING);
                for (var o : others) {
                    o.setStatus(RequestStatus.REJECTED);
                    requestRepo.save(o);
                }
            } else if (newStatus == RequestStatus.PENDING) {
                req.setStatus(RequestStatus.PENDING);
            } else if (newStatus == RequestStatus.ACCEPTED) {
                // if you allow direct ACCEPTED (skipping negotiation)
                boolean alreadyAccepted = requestRepo.existsByMobile_MobileIdAndStatus(req.getMobile().getMobileId(), RequestStatus.ACCEPTED);
                if (alreadyAccepted)
                    throw new MobileRequestException("Another request already ACCEPTED for this mobile.");
                req.setStatus(RequestStatus.ACCEPTED);
                req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.ACTIVE);
                mobileRepo.save(req.getMobile());
            }
            requestRepo.save(req);
        }

        return toResponse(req);
    }

    @Override
    @Transactional
    public MobileRequestResponseDTO appendMessage(Long requestId, Long senderUserId, String message) {
        MobileRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        User sender = userRepo.findById(senderUserId)
                .orElseThrow(() -> new MobileRequestException("Sender user not found: " + senderUserId));

        String senderType = determineSenderType(senderUserId, req);
        appendMessageInternal(req, senderUserId, senderType, message);
        requestRepo.save(req);
        return toResponse(req);
    }

    // Mark accepted or in negotiation request completed & mark mobile sold
    @Override
    @Transactional
    public void markRequestCompletedAndMarkSold(Long requestId) {
        MobileRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        synchronized (("mobile-lock-" + req.getMobile().getMobileId()).intern()) {
            if (req.getStatus() != RequestStatus.ACCEPTED && req.getStatus() != RequestStatus.IN_NEGOTIATION) {
                throw new MobileRequestException("Request must be ACCEPTED or IN_NEGOTIATION to mark completed.");
            }
            req.setStatus(RequestStatus.COMPLETED);
            req.getMobile().setStatus(com.spring.jwt.Mobile.entity.Mobile.Status.SOLD);
            mobileRepo.save(req.getMobile());

            // reject other requests
            var others = requestRepo.findByMobile_MobileIdAndStatus(req.getMobile().getMobileId(), RequestStatus.PENDING);
            for (var o : others) {
                o.setStatus(RequestStatus.REJECTED);
                requestRepo.save(o);
            }
            requestRepo.save(req);
        }
    }

    // ---------------- helpers ----------------
    private void appendMessageInternal(MobileRequest req, Long senderId, String senderType, String text) {
        try {
            List<ConversationMessage> msgs = objectMapper.readValue(
                    req.getConversation(), new TypeReference<List<ConversationMessage>>() {}
            );
            if (msgs == null) msgs = new ArrayList<>();

            // ⭐ Fetch sender user to get the full name
            User sender = userRepo.findById(senderId)
                    .orElseThrow(() -> new MobileRequestException("Sender user not found: " + senderId));

            String senderName =
                    (sender.getFirstName() == null ? "" : sender.getFirstName()) + " " +
                            (sender.getLastName() == null ? "" : sender.getLastName());

            // ⭐ Create message with senderName included
            ConversationMessage cm = new ConversationMessage();
            cm.setSenderId(senderId);
            cm.setSenderType(senderType);
            cm.setMessage(text);
            cm.setTimestamp(OffsetDateTime.now());
            cm.setSenderName(senderName.trim());

            msgs.add(cm);

            req.setConversation(objectMapper.writeValueAsString(msgs));

        } catch (Exception e) {
            throw new MobileRequestException("Failed to append message", e);
        }
    }


    private String determineSenderType(Long senderUserId, MobileRequest req) {
        try {
            // 🔹 Buyer side check
            if (req.getBuyer() != null && req.getBuyer().getUser() != null) {
                Long buyerUserId = req.getBuyer().getUser().getId();
                if (senderUserId.equals(buyerUserId)) {
                    return "BUYER";
                }
            }

            // 🔹 Seller side check
            if (req.getSeller() != null && req.getSeller().getUser() != null) {
                Long sellerUserId = req.getSeller().getUser().getId();
                if (senderUserId.equals(sellerUserId)) {
                    return "SELLER";
                }
            }

        } catch (Exception e) {
            throw new MobileRequestException("Failed to determine sender type", e);
        }

        return "UNKNOWN";
    }




    private MobileRequestResponseDTO toResponse(MobileRequest req) {
        MobileRequestResponseDTO dto = new MobileRequestResponseDTO();

        dto.setRequestId(req.getRequestId());
        dto.setMobileId(req.getMobile().getMobileId());
        dto.setBuyerId(req.getBuyer().getBuyerId());
        dto.setSellerId(req.getSeller().getSellerId());

        // buyer name from Buyer → User
        if (req.getBuyer() != null && req.getBuyer().getUser() != null) {
            var u = req.getBuyer().getUser();
            String fullName = (u.getFirstName() != null ? u.getFirstName() : "") +
                    " " +
                    (u.getLastName() != null ? u.getLastName() : "");
            dto.setBuyerName(fullName.trim());
        }

        // seller name from Seller → User
        if (req.getSeller() != null && req.getSeller().getUser() != null) {
            var u = req.getSeller().getUser();
            String fullName = (u.getFirstName() != null ? u.getFirstName() : "") +
                    " " +
                    (u.getLastName() != null ? u.getLastName() : "");
            dto.setSellerName(fullName.trim());
        }

        dto.setStatus(req.getStatus().name());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());

        // Raw JSON (optional, for debugging)
//        dto.setConversationJson(req.getConversation());

        // Convert JSON string → List<ConversationMessage>
        try {
            if (req.getConversation() != null && !req.getConversation().isEmpty()) {
                List<ConversationMessage> messages = objectMapper.readValue(
                        req.getConversation(),
                        new TypeReference<List<ConversationMessage>>() {
                        }
                );
                dto.setConversation(messages);
            }
        } catch (Exception e) {
            dto.setConversation(null);
        }

        return dto;
    }
}