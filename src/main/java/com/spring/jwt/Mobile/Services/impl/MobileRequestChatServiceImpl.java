package com.spring.jwt.Mobile.Services.impl;

import com.spring.jwt.Mobile.Mapper.ChatMapper;
import com.spring.jwt.Mobile.Repository.ChatMessageRepository;
import com.spring.jwt.Mobile.Repository.MobileRequestRepository;
import com.spring.jwt.Mobile.Services.MobileRequestChatService;
import com.spring.jwt.Mobile.dto.ChatMessageDTO;
import com.spring.jwt.Mobile.dto.ChatSendDTO;
import com.spring.jwt.Mobile.entity.ChatMessage;
import com.spring.jwt.Mobile.entity.MobileRequest;
import com.spring.jwt.Mobile.entity.RequestStatus;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.mobilechat.ChatNotAllowedException;
import com.spring.jwt.exception.mobilechat.ChatRequestNotFoundException;
import com.spring.jwt.exception.mobilechat.ChatSenderNotFoundException;
import com.spring.jwt.exception.mobilechat.ChatUnauthorizedSenderException;
import com.spring.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MobileRequestChatServiceImpl implements MobileRequestChatService {

    private final MobileRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final ChatMessageRepository chatRepo;

    // ================= SEND MESSAGE =================

    @Override
    public ChatMessageDTO sendMessage(ChatSendDTO dto) {

        MobileRequest request = requestRepo.findById(dto.getRequestId())
                .orElseThrow(() ->
                        new ChatRequestNotFoundException(dto.getRequestId()));

        // Chat allowed only after seller accepts
        if (request.getStatus() != RequestStatus.IN_NEGOTIATION) {
            throw new ChatNotAllowedException(
                    "Chat allowed only when request is IN_NEGOTIATION"
            );
        }

        User sender = userRepo.findById(dto.getSenderUserId())
                .orElseThrow(() ->
                        new ChatSenderNotFoundException(dto.getSenderUserId()));

        validateSender(request, sender);

        ChatMessage message = ChatMessage.builder()
                .request(request)
                .sender(sender)
                .message(dto.getMessage())
                .build();

        chatRepo.save(message);

        boolean isBuyer =
                sender.getId().equals(request.getBuyer().getUser().getId());

        Long receiverUserId = isBuyer
                ? request.getSeller().getUser().getId()
                : request.getBuyer().getUser().getId();

        String senderRole = isBuyer ? "BUYER" : "SELLER";

        return ChatMapper.toDTO(
                message,
                senderRole,
                receiverUserId
        );
    }

    // ================= GET CHAT HISTORY =================

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<ChatMessageDTO> getChat(Long requestId) {

        MobileRequest request = requestRepo.findById(requestId)
                .orElseThrow(() ->
                        new ChatRequestNotFoundException(requestId));

        return chatRepo
                .findByRequest_RequestIdOrderByCreatedAtAsc(requestId)
                .stream()
                .map(msg -> {

                    boolean isBuyer =
                            msg.getSender().getId()
                                    .equals(request.getBuyer().getUser().getId());

                    String role = isBuyer ? "BUYER" : "SELLER";

                    Long receiverId = isBuyer
                            ? request.getSeller().getUser().getId()
                            : request.getBuyer().getUser().getId();

                    return ChatMapper.toDTO(msg, role, receiverId);
                })
                .toList();
    }

    // ================= VALIDATION =================

    public void validateSender(MobileRequest req, User sender) {

        if (req.getBuyer() == null || req.getSeller() == null) {
            throw new ChatUnauthorizedSenderException("Invalid request participants");
        }

        Long senderId = sender.getId();
        Long buyerUserId = req.getBuyer().getUser().getId();
        Long sellerUserId = req.getSeller().getUser().getId();

        if (!senderId.equals(buyerUserId) && !senderId.equals(sellerUserId)) {
            throw new ChatUnauthorizedSenderException(
                    "User is not part of this request"
            );
        }
    }

}
