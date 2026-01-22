package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRequest_RequestIdOrderByCreatedAtAsc(Long requestId);
}

