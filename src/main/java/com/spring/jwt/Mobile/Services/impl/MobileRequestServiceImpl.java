package com.spring.jwt.Mobile.Services.impl ;

import com.spring.jwt.Mobile.dto.*;
import com.spring.jwt.Mobile.entity.*;
import com.spring.jwt.exception.mobile.*;
import com.spring.jwt.Mobile.Repository.*;
import com.spring.jwt.Mobile.Services.MobileRequestService;
import com.spring.jwt.repository.BuyerRepository; // assume buyer/user repo exists
import com.spring.jwt.exception.mobile.MobileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileRequestServiceImpl implements MobileRequestService {

    private final MobileRequestRepository mobileRequestRepository;
    private final MobileRepository mobileRepository;
    private final MobileRequestMessageRepository messageRepository;
    private final BuyerRepository buyerRepository; // or BuyerRepository if exists

    // Create a new interest request (buyer)
    @Override
    @Transactional
    public MobileReqResponseDTO createRequest(ConversationMessage dto) {
        var mobile = mobileRepository.findByMobileIdAndDeletedFalse(dto.getMobileId())
                .orElseThrow(() -> new MobileNotFoundException(dto.getMobileId()));

        if (mobile.getStatus() == Mobile.Status.SOLD) {
            throw new MobileRequestException("Product already sold.");
        }

        // optional: verify buyer exists
        if (dto.getBuyerId() == null || !buyerRepository.existsById(dto.getBuyerId())) {
            throw new MobileRequestException("Buyer not found: " + dto.getBuyerId());
        }

        MobileRequest req = MobileRequest.builder()
                .mobile(mobile)
                .buyerId(dto.getBuyerId())
                .initialMessage(dto.getMessage())
                .status(MobileRequest.Status.PENDING)
                .build();

        mobileRequestRepository.save(req);
        return toResponse(req);
    }

    // list pending/others for a mobile
    @Override
    public List<MobileReqResponseDTO> listRequestsForMobile(Long mobileId) {
        return mobileRequestRepository.findByMobile_MobileIdOrderByCreatedAtAsc(mobileId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MobileReqResponseDTO> listRequestsForBuyer(Long buyerId) {
        return mobileRequestRepository.findByBuyerId(buyerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Update a request status (accept/reject/negotiation start/completed)
    @Override
    @Transactional
    public MobileReqResponseDTO updateRequestStatus(Long requestId, String statusStr) {
        MobileRequest req = mobileRequestRepository.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        var mobile = req.getMobile();
        MobileRequest.Status newStatus;
        try {
            newStatus = MobileRequest.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new MobileRequestException("Invalid status: " + statusStr);
        }

        // synchronize by mobile to avoid races across threads in this JVM
        synchronized (("mobile-lock-" + mobile.getMobileId()).intern()) {
            if (newStatus == MobileRequest.Status.ACCEPTED) {
                // DB check: prevent double-accept
                boolean exists = mobileRequestRepository.existsByMobile_MobileIdAndStatus(mobile.getMobileId(), MobileRequest.Status.ACCEPTED);
                if (exists) {
                    throw new MobileRequestException("Another request is already accepted for this product.");
                }
                req.setStatus(MobileRequest.Status.ACCEPTED);
                mobile.setStatus(Mobile.Status.ACTIVE); // negotiation / hold
                mobileRepository.save(mobile);
            } else if (newStatus == MobileRequest.Status.REJECTED) {
                req.setStatus(MobileRequest.Status.REJECTED);
                // if no accepted requests exist, we keep mobile AVAILABLE
                boolean anyAccepted = mobileRequestRepository.existsByMobile_MobileIdAndStatus(mobile.getMobileId(), MobileRequest.Status.ACCEPTED);
                if (!anyAccepted && mobile.getStatus() != Mobile.Status.SOLD) {
                    mobile.setStatus(Mobile.Status.ACTIVE); // remain active or AVAILABLE depending on your logic
                    // you may set AVAILABLE enum if you have it; using ACTIVE to indicate still listed
                    mobileRepository.save(mobile);
                }
            } else if (newStatus == MobileRequest.Status.IN_NEGOTIATION) {
                req.setStatus(MobileRequest.Status.IN_NEGOTIATION);
                // optionally set mobile status
                mobile.setStatus(Mobile.Status.ACTIVE);
                mobileRepository.save(mobile);
            } else if (newStatus == MobileRequest.Status.COMPLETED) {
                // completing a request should mark mobile sold
                req.setStatus(MobileRequest.Status.COMPLETED);
                mobile.setStatus(Mobile.Status.SOLD);
                mobileRepository.save(mobile);

                // set all other requests to REJECTED
                List<MobileRequest> others = mobileRequestRepository.findByMobile_MobileIdAndStatus(mobile.getMobileId(), MobileRequest.Status.PENDING);
                for (MobileRequest other : others) {
                    other.setStatus(MobileRequest.Status.REJECTED);
                    mobileRequestRepository.save(other);
                }
            } else {
                // PENDING or other statuses
                req.setStatus(newStatus);
            }

            mobileRequestRepository.save(req);
        }

        return toResponse(req);
    }

    // Send a message within a request (buyer or seller)
    @Override
    @Transactional
    public void sendMessage(Long requestId, Long senderId, String message) {
        MobileRequest req = mobileRequestRepository.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        // optional: check sender is either buyer or seller (if sellerId exists)
        MobileRequestMessage msg = MobileRequestMessage.builder()
                .request(req)
                .senderId(senderId)
                .message(message)
                .build();
        messageRepository.save(msg);
    }

    @Override
    public List<MobileRequestMessageDTO> listMessages(Long requestId) {
        return messageRepository.findByRequest_RequestIdOrderBySentAtAsc(requestId)
                .stream().map(m -> {
                    MobileRequestMessageDTO dto = new MobileRequestMessageDTO();
                    dto.setMessageId(m.getMessageId());
                    dto.setRequestId(m.getRequest().getRequestId());
                    dto.setSenderId(m.getSenderId());
                    dto.setMessage(m.getMessage());
                    dto.setSentAt(m.getSentAt());
                    return dto;
                }).collect(Collectors.toList());
    }

    // Mark mobile sold using an accepted request. This will set mobile.SOLD and other requests -> REJECTED
    @Override
    @Transactional
    public void markMobileSold(Long requestId) {
        MobileRequest req = mobileRequestRepository.findById(requestId)
                .orElseThrow(() -> new MobileRequestNotFoundException(requestId));

        synchronized (("mobile-lock-" + req.getMobile().getMobileId()).intern()) {
            // ensure this request is ACCEPTED or will be accepted now
            if (req.getStatus() != MobileRequest.Status.ACCEPTED && req.getStatus() != MobileRequest.Status.IN_NEGOTIATION) {
                throw new MobileRequestException("Cannot mark sold: request is not accepted or in negotiation.");
            }

            // mark request completed and mobile sold
            req.setStatus(MobileRequest.Status.COMPLETED);
            Mobile mobile = req.getMobile();
            mobile.setStatus(Mobile.Status.SOLD);
            mobileRepository.save(mobile);
            mobileRequestRepository.save(req);

            // reject all other pending requests
            List<MobileRequest> others = mobileRequestRepository.findByMobile_MobileIdOrderByCreatedAtAsc(mobile.getMobileId());
            for (MobileRequest other : others) {
                if (!other.getRequestId().equals(requestId)) {
                    other.setStatus(MobileRequest.Status.REJECTED);
                    mobileRequestRepository.save(other);
                }
            }
        }
    }

    // helper mapping
    private MobileReqResponseDTO toResponse(MobileRequest r) {
        return MobileReqResponseDTO.builder()
                .requestId(r.getRequestId())
                .mobileId(r.getMobile().getMobileId())
                .buyerId(r.getBuyerId())
                .initialMessage(r.getInitialMessage())
                .status(r.getStatus().name())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
