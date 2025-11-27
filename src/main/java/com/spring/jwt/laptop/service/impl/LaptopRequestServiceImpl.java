package com.spring.jwt.laptop.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.entity.Status;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.bookings.LaptopRequestException;
import com.spring.jwt.exception.bookings.LaptopRequestNotFoundException;
import com.spring.jwt.laptop.dto.*;
import com.spring.jwt.laptop.entity.LaptopBooking;
import com.spring.jwt.laptop.model.LaptopRequestStatus;
import com.spring.jwt.laptop.repository.LaptopRepository;
import com.spring.jwt.laptop.repository.LaptopRequestRepository;
import com.spring.jwt.laptop.service.LaptopRequestService;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.repository.SellerRepository;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaptopRequestServiceImpl implements LaptopRequestService {

    private final LaptopRequestRepository requestRepo;
    private final LaptopRepository laptopRepo;
    private final BuyerRepository buyerRepo;
    private final SellerRepository sellerRepo;
    private final UserRepository userRepo;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    @Transactional
    public LaptopRequestResponseDTO createRequest(LaptopRequestCreateDTO dto) {
        var laptop = laptopRepo.findByIdAndDeletedFalse(dto.getLaptopId())
                .orElseThrow(() -> new LaptopRequestException("Laptop not found: " + dto.getLaptopId()));

        if (laptop.getStatus() != null && laptop.getStatus().name().equalsIgnoreCase("SOLD")) {
            throw new LaptopRequestException("Product already sold.");
        }

        User buyerUser = userRepo.findById(dto.getBuyerUserId())
                .orElseThrow(() -> new LaptopRequestException("Buyer (user) not found: " + dto.getBuyerUserId()));

        Buyer buyer = buyerRepo.findByUser_IdAndDeletedFalse(dto.getBuyerUserId())
                .orElseThrow(() -> new LaptopRequestException("User is not registered as a buyer: " + dto.getBuyerUserId()));

        Seller seller = laptop.getSeller();
        if (seller == null) throw new LaptopRequestException("Laptop has no seller assigned");

        boolean alreadyRequested = requestRepo.findByBuyer_BuyerId(buyer.getBuyerId())
                .stream().anyMatch(r -> r.getLaptop().getId().equals(laptop.getId()));
        if (alreadyRequested) {
            throw new LaptopRequestException("You have already sent a request for this product.");
        }

        if (dto.getBookingDate() == null || dto.getBookingDate().isBefore(LocalDate.now())) {
            throw new LaptopRequestException("Booking date must be today or in the future.");
        }



        LaptopBooking r = LaptopBooking.builder()
                .laptop(laptop)
                .buyer(buyer)
                .seller(seller)
                .onDate(dto.getBookingDate())
                .pendingStatus(LaptopRequestStatus.PENDING)
                .requestConversation("[]")
                .build();

        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            appendMessageInternal(r, buyerUser.getId(), "BUYER", dto.getMessage());
        }

        try {
            r = requestRepo.save(r);
        } catch (DataIntegrityViolationException e) {
            throw new LaptopRequestException("Failed to create request: " + e.getMessage());
        }

        return toResponse(r);
    }

    @Override
    public List<LaptopRequestResponseDTO> listRequestsForLaptop(Long laptopId) {
        laptopRepo.findById(laptopId)
                .orElseThrow(() -> new LaptopRequestException("Laptop with ID " + laptopId + " not found."));

        List<LaptopBooking> requests = requestRepo.findByLaptop_IdOrderByCreatedAtAsc(laptopId);

        if (requests.isEmpty()) {
            throw new LaptopRequestException("No requests found for laptop ID " + laptopId + ".");
        }

        return requests.stream().map(this::toResponse).toList();
    }

    @Override
    public List<LaptopRequestResponseDTO> listRequestsForBuyer(Long buyerId) {
        buyerRepo.findById(buyerId)
                .orElseThrow(() -> new LaptopRequestException("Buyer with ID " + buyerId + " not found."));

        List<LaptopBooking> requests = requestRepo.findByBuyer_BuyerId(buyerId);

        if (requests.isEmpty()) {
            throw new LaptopRequestException("No requests found for buyer ID " + buyerId + ".");
        }

        return requests.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public LaptopRequestResponseDTO updateRequestStatus(Long requestId, String statusStr) {
        LaptopBooking req = requestRepo.findById(requestId)
                .orElseThrow(() -> new LaptopRequestNotFoundException(requestId));

        LaptopRequestStatus newStatus;
        try {
            newStatus = LaptopRequestStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new LaptopRequestException("Invalid status: " + statusStr);
        }

        synchronized (("laptop-lock-" + req.getLaptop().getId()).intern()) {
            if (newStatus == LaptopRequestStatus.ACCEPTED) {
                boolean exists = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.ACCEPTED);
                if (exists) throw new LaptopRequestException("Another request already ACCEPTED for this laptop.");

                req.setPendingStatus(LaptopRequestStatus.ACCEPTED);

                req.getLaptop().setStatus(Status.ACTIVE);
                laptopRepo.save(req.getLaptop());

            } else if (newStatus == LaptopRequestStatus.REJECTED) {
                boolean exists = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.REJECTED);
                if (exists) throw new LaptopRequestException("Request already REJECTED for this laptop.");
                req.setPendingStatus(LaptopRequestStatus.REJECTED);

                boolean anyAccepted = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.ACCEPTED);
                if (!anyAccepted && req.getLaptop().getStatus() != Status.SOLD) {
                    req.getLaptop().setStatus(Status.ACTIVE);
                    laptopRepo.save(req.getLaptop());
                }

            } else if (newStatus == LaptopRequestStatus.COMPLETED) {
                boolean exists = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.COMPLETED);
                if (exists) throw new LaptopRequestException("Request already COMPLETED for this laptop.");

                req.setPendingStatus(LaptopRequestStatus.COMPLETED);
                req.getLaptop().setStatus(Status.SOLD);
                laptopRepo.save(req.getLaptop(


                ));
                var others = requestRepo.findByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.PENDING);
                for (var o : others) {
                    o.setPendingStatus(LaptopRequestStatus.REJECTED);
                    requestRepo.save(o);
                }

            } else if(newStatus == LaptopRequestStatus.IN_NEGOTIATION){
                boolean exists = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.IN_NEGOTIATION);
                if (exists) throw new LaptopRequestException("Laptop is already is in IN_NEGOTIATION.");

                req.setPendingStatus(LaptopRequestStatus.IN_NEGOTIATION);
                req.getLaptop().setStatus(Status.ACTIVE);
                laptopRepo.save(req.getLaptop());

            }
            else if(newStatus == LaptopRequestStatus.PENDING){
                boolean exists = requestRepo.existsByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.PENDING);
                if (exists) throw new LaptopRequestException("Laptop request is already PENDING.");

                req.setPendingStatus(LaptopRequestStatus.PENDING);
                req.getLaptop().setStatus(Status.ACTIVE);
                laptopRepo.save(req.getLaptop());
            }
            else {
                req.setPendingStatus(newStatus);
            }

            requestRepo.save(req);
        }

        return toResponse(req);
        }


    @Override
    @Transactional
    public LaptopRequestResponseDTO appendMessage(Long requestId, Long senderUserId, String message) {
        LaptopBooking req = requestRepo.findById(requestId)
                .orElseThrow(() -> new LaptopRequestNotFoundException(requestId));

        User sender = userRepo.findById(senderUserId)
                .orElseThrow(() -> new LaptopRequestException("Sender user not found: " + senderUserId));

        String senderType = determineSenderType(senderUserId, req);
        appendMessageInternal(req, senderUserId, senderType, message);
        requestRepo.save(req);
        return toResponse(req);
    }

    @Override
    @Transactional
    public void markRequestCompletedAndMarkSold(Long requestId) {
        LaptopBooking req = requestRepo.findById(requestId)
                .orElseThrow(() -> new LaptopRequestNotFoundException(requestId));

        synchronized (("laptop-lock-" + req.getLaptop().getId()).intern()) {
            if (req.getPendingStatus() != LaptopRequestStatus.ACCEPTED && req.getPendingStatus() != LaptopRequestStatus.IN_NEGOTIATION) {
                throw new LaptopRequestException("Request must be ACCEPTED or IN_NEGOTIATION to mark completed.");
            }
            req.setPendingStatus(LaptopRequestStatus.COMPLETED);
            req.getLaptop().setStatus(Status.SOLD);
            laptopRepo.save(req.getLaptop());
            var others = requestRepo.findByLaptop_IdAndPendingStatus(req.getLaptop().getId(), LaptopRequestStatus.PENDING);
            for (var o : others) {
                o.setPendingStatus(LaptopRequestStatus.REJECTED);
                requestRepo.save(o);
            }
            requestRepo.save(req);
        }
    }

    private void appendMessageInternal(LaptopBooking req, Long senderId, String senderType, String text) {
        try {
            List<ConversationMessageDTO> msgs = objectMapper.readValue(
                    req.getRequestConversation(), new TypeReference<List<ConversationMessageDTO>>() {
                    });
            if (msgs == null) msgs = new ArrayList<>();
            msgs.add(new ConversationMessageDTO(senderId, senderType, text, OffsetDateTime.now()));
            req.setRequestConversation(objectMapper.writeValueAsString(msgs));
        } catch (Exception e) {
            throw new LaptopRequestException("Failed to append message");
        }
    }

    private String determineSenderType(Long userId, LaptopBooking req) {
        if (req.getBuyer() != null && req.getBuyer().getUser() != null &&
                req.getBuyer().getUser().getId().equals(userId)) return "BUYER";
        if (req.getSeller() != null && req.getSeller().getUser() != null &&
                req.getSeller().getUser().getId().equals(userId)) return "SELLER";
        return "UNKNOWN";
    }

    private LaptopRequestResponseDTO toResponse(LaptopBooking r) {
        return LaptopRequestResponseDTO.builder()
                .laptopBookingId(r.getLaptopBookingId())
                .laptopId(r.getLaptop().getId())
                .buyerId(r.getBuyer().getBuyerId())
                .sellerId(r.getSeller().getSellerId())
                .status(r.getPendingStatus().name())
                .conversationJson(r.getRequestConversation())
                .createdAt(r.getCreatedAt())
                .bookingDate(r.getOnDate())
                .build();
    }

}
